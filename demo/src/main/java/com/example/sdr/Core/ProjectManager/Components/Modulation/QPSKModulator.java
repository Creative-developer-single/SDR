package com.example.sdr.Core.ProjectManager.Components.Modulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.ShapeFilter.RRCFilter;

/**
 * QPSK Modulator
 *
 * This component takes a binary data stream (+1.0 / -1.0) and performs QPSK modulation.
 * It uses a time-driven interpolation approach, similar to the RandomNumGenerator, to
 * correctly handle the rate change from the input symbol rate to the output sample rate.
 * The process is as follows:
 * 1. Bits are grouped into pairs for I (In-phase) and Q (Quadrature) channels.
 * 2. An NRZ (Non-Return-to-Zero) complex baseband signal is generated at the project's sample rate.
 * 3. This signal is passed through an RRC filter for pulse shaping.
 * 4. The shaped baseband signal is then modulated by a complex carrier.
 */
public class QPSKModulator extends BaseComponent {

    // ================== Custom Properties ==================
    /**
     * Carrier Frequency (Hz)
     */
    private double CarrierFrequency = 10000.0;
    /**
     * Symbol Rate (symbols per second). Note: Bit Rate = 2 * Rs for QPSK.
     */
    private double Rs = 1000.0;
    /**
     * Type of the shaping filter (e.g., "RRC")
     */
    private String FilterType = "RRC";
    /**
     * Alpha (roll-off factor) for the shaping filter
     */
    private double FilterAlpha = 0.35;

    // ================== Internal Components ==================
    private RRCFilter rrcFilter;

    // ================== Internal State Variables ==================
    /**
     * Symbol phase accumulator (normalized to symbol period, 0.0 to 1.0)
     */
    private double symbolPhase = 0.0;
    /**
     * Phase increment per output sample (Rs / SampleRate)
     */
    private double symbolPhaseIncrement = 0.0;
    /**
     * Index for the current bit being processed from the input buffer
     */
    private int inputBitIndex = 0;
    /**
     * Current value for the In-phase component (+1.0 or -1.0)
     */
    private double currentIValue = 1.0;
    /**
     * Current value for the Quadrature component (+1.0 or -1.0)
     */
    private double currentQValue = 1.0;

    /**
     * Phase accumulator for the carrier frequency oscillator
     */
    private double carrierPhase = 0.0;

    /**
     * Phase increment per sample for the carrier oscillator
     */
    private double carrierPhaseIncrement = 0.0;


    // ================== Constructor ==================
    public QPSKModulator(int blockLength, int inputCount, int outputCount, String ID) {
        // As a modulator, it has 1 input and 1 output
        super(blockLength, 1, 1, ID);
        this.Type = "Modulator";
        // Initialize the component with default values
        this.refreshComponent();
    }

    // ================== Core Calculation Method ==================
    @Override
    public void Calculate() {
        // Ensure input exists and is valid
        if (this.op_in[0] == null || this.op_in[0].length == 0) {
            // If no input, output zeros
             for (int i = 0; i < this.blockLength; i++) {
                this.ans[0][i].fromComplex(0,0);
            }
            return;
        }

        // Step 1: Create a complex baseband signal with NRZ pulses (rate-adapted)
        SDRData[] baseband_nrz = SDRDataUtils.createComplexArray(this.blockLength, 0, 0);

        for (int i = 0; i < this.blockLength; i++) {
            // Advance the symbol phase based on the output sample clock
            this.symbolPhase += this.symbolPhaseIncrement;

            // If phase crosses a symbol boundary, grab the next two bits for I and Q
            if (this.symbolPhase >= 1.0) {
                this.symbolPhase -= 1.0; // Wrap phase

                // Check if there are enough bits left in the input buffer
                if (this.inputBitIndex + 1 < this.op_in[0].length) {
                    // Map even bit to I, odd bit to Q
                    this.currentIValue = this.op_in[0][this.inputBitIndex].getReal();
                    this.currentQValue = this.op_in[0][this.inputBitIndex + 1].getReal();
                    this.inputBitIndex += 2; // Consume two bits
                }
                // If we run out of input bits, just hold the last I/Q value
            }
            // Set the output sample to the current I/Q value (as a complex number)
            baseband_nrz[i].fromComplex(this.currentIValue, this.currentQValue);
        }

        // Step 2: Pass the NRZ signal through the RRC filter for pulse shaping
        this.rrcFilter.setOperationParams(baseband_nrz, 0);
        this.rrcFilter.Calculate();
        SDRData[] baseband_shaped = this.rrcFilter.getAns(0);


        // Step 3: Modulate the shaped baseband signal onto the carrier
        for (int i = 0; i < this.blockLength; i++) {
            double i_shaped = baseband_shaped[i].getReal();
            double q_shaped = baseband_shaped[i].getImag();

            // Generate carrier signal: e^(j*phase) = cos(phase) + j*sin(phase)
            double cos_c = Math.cos(this.carrierPhase);
            double sin_c = Math.sin(this.carrierPhase);

            // Perform complex multiplication: (I + jQ) * (cos + j*sin)
            // Real part: I*cos - Q*sin
            // Imag part: I*sin + Q*cos
            double real_part = i_shaped * cos_c - q_shaped * sin_c;
            double imag_part = i_shaped * sin_c + q_shaped * cos_c;

            this.ans[0][i].fromComplex(real_part, imag_part);
            this.ans[0][i].setComputeMode(SDRData.ComputeMode.COMPLEX);


            // Advance carrier phase for the next sample
            this.carrierPhase += this.carrierPhaseIncrement;
        }

        // Wrap carrier phase to prevent overflow and maintain precision
        if (this.carrierPhase > 2 * Math.PI) {
            this.carrierPhase -= 2 * Math.PI;
        }
    }

    // ================== Refresh Component State ==================
    @Override
    public void refreshComponent() {
        // Reset state variables
        this.symbolPhase = 0.0;
        this.inputBitIndex = 0;
        this.carrierPhase = 0.0;
        
        // Initialize with a default starting symbol
        this.currentIValue = 1.0;
        this.currentQValue = 1.0;

        // Recalculate increments if rates are valid
        if (this.Rs > 0 && this.SampleRate > 0) {
            this.symbolPhaseIncrement = this.Rs / this.SampleRate;
            this.carrierPhaseIncrement = 2.0 * Math.PI * this.CarrierFrequency / this.SampleRate;
        } else {
            this.symbolPhaseIncrement = 0.0;
            this.carrierPhaseIncrement = 0.0;
        }

        // (Re)initialize the RRC filter with current parameters
        // Note: The RRCFilter's constructor might need different parameters depending on its implementation.
        // For example, it typically requires number of taps and samples per symbol.
        int samplesPerSymbol = (int)(this.SampleRate / this.Rs);
        int filterTaps = samplesPerSymbol * 8; // A common choice: 8 symbols of filter length
        this.rrcFilter = new RRCFilter(this.blockLength, this.SampleRate, this.Rs, filterTaps, this.FilterAlpha, this.FilterType, this.ID + "-RRC");
        
        // Reset output array
        resetBlockLength(blockLength);
    }

    // ================== Modify Properties Interface ==================
    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            switch (name) {
                case "Rs":
                    this.setRs(Double.parseDouble(value.toString()));
                    return true;
                case "CarrierFrequency":
                    this.setCarrierFrequency(Double.parseDouble(value.toString()));
                    return true;
                case "FilterAlpha":
                    this.setFilterAlpha(Double.parseDouble(value.toString()));
                    return true;
            }
            return super.ModifyPropertiesByName(name, value);
        } catch (Exception e) {
            System.err.println("Failed to modify property: " + name + ", Value: " + value + ". Error: " + e.getMessage());
            return false;
        }
    }
    
    // ================== Adaptive Rate Handling ==================
    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // Reset bit index when new data arrives
        this.inputBitIndex = 0;
        
        // This component does not change its output block length based on input length,
        // as it performs rate conversion. We just assign the new data.
        this.op_in[index] = data;
    }

    // ================== Getters and Setters ==================

    public double getCarrierFrequency() {
        return CarrierFrequency;
    }

    public void setCarrierFrequency(double carrierFrequency) {
        if (carrierFrequency < 0) {
            throw new IllegalArgumentException("Carrier frequency must be a non-negative number.");
        }
        this.CarrierFrequency = carrierFrequency;
        this.refreshComponent();
    }

    public double getRs() {
        return Rs;
    }

    public void setRs(double rs) {
        if (rs <= 0) {
            throw new IllegalArgumentException("Rs (symbol rate) must be a positive number.");
        }
        this.Rs = rs;
        this.refreshComponent();
    }

    public String getFilterType() {
        return FilterType;
    }

    public void setFilterType(String filterType) {
        this.FilterType = filterType;
        this.refreshComponent();
    }

    public double getFilterAlpha() {
        return FilterAlpha;
    }

    public void setFilterAlpha(double filterAlpha) {
         if (filterAlpha < 0 || filterAlpha > 1) {
            throw new IllegalArgumentException("Filter alpha (roll-off) must be between 0 and 1.");
        }
        this.FilterAlpha = filterAlpha;
        this.refreshComponent();
    }
}