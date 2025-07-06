package com.example.sdr.Core.ProjectManager.Components.DeModulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Arithmetic.NonLinear;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.FIR.FIRLowPass;

/**
 * AM Demodulator (Envelope Detector)
 *
 * This component demodulates an Amplitude Modulated (AM) signal using an
 * envelope detector. It is a composite component that internally uses:
 * 1. A NonLinear component to rectify the input signal.
 * 2. An FIRLowPass filter to smooth the rectified signal and recover the baseband message.
 */
public class AMDemodulator extends BaseComponent {

    // ================== Custom Properties ==================

    /**
     * The expected carrier frequency of the input signal. This is for informational
     * purposes and to guide the choice of the cutoff frequency.
     */
    private double CarrierFrequency;

    /**
     * The cutoff frequency for the internal low-pass filter, used to
     * separate the message signal from the carrier frequency after rectification.
     * This should be set higher than the message bandwidth but well below the carrier frequency.
     */
    private double CutoffFrequency;

    // ================== Internal Components ==================

    /**
     * Internal NonLinear component used for signal rectification (the first step of envelope detection).
     */
    private final NonLinear nonLinearComponent;

    /**
     * Internal LowPass filter used to extract the envelope (the second step).
     */
    private final FIRLowPass lowPassFilter;


    // ================== Constructor ==================

    public AMDemodulator(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, 1, 1, ID); // AM Demodulator has 1 input and 1 output
        this.Type = "Demodulator";

        // Initialize default properties
        this.CarrierFrequency = 10000.0; // Default carrier frequency: 10 kHz
        this.CutoffFrequency = 2000.0;  // Default cutoff frequency: 2 kHz

        // Instantiate internal components
        // The rectifier takes the input signal and has one output
        this.nonLinearComponent = new NonLinear(blockLength, 1, 1, ID + "-Rectifier");
        // The LPF takes the rectified signal and produces the final output
        this.lowPassFilter = new FIRLowPass(blockLength, 1, 1, ID + "-LPF");

        // Configure the components with default settings
        this.refreshComponent();
    }


    // ================== Core Calculation Method ==================

    @Override
    public void Calculate() {
        // Ensure input is valid
        if (this.op_in[0] == null) {
            return;
        }

        // --- Signal Processing Chain ---

        // Step 1: Rectify the input signal to get its envelope.
        nonLinearComponent.setOperationParams(this.op_in[0], 0);
        nonLinearComponent.Calculate();
        SDRData[] rectifiedSignal = nonLinearComponent.getAns(0);

        // Step 2: Pass the rectified signal through a low-pass filter to remove the carrier
        // and isolate the baseband message.
        lowPassFilter.setOperationParams(rectifiedSignal, 0);
        lowPassFilter.Calculate();
        SDRData[] demodulatedSignal = lowPassFilter.getAns(0);
        
        // Step 3: DC Removal (Optional but recommended)
        // A DC blocking filter could be added here to remove any DC offset from the carrier.
        // For now, we will just output the LPF result.

        // Copy the final result to the output buffer of this component
        for (int i = 0; i < this.blockLength; i++) {
            this.ans[0][i].Copy(demodulatedSignal[i]);
            // The output of an AM demodulator is a real signal
            this.ans[0][i].setComputeMode(SDRData.ComputeMode.REAL);
        }
    }

    // ================== Refresh Component State ==================

    @Override
    public void refreshComponent() {
        // Configure the NonLinear component to act as a rectifier
        // "Abs" is often preferred for envelope detection as it's a full-wave rectifier
        nonLinearComponent.setOperationMode("Abs");
        nonLinearComponent.refreshComponent();

        // Configure the FIRLowPass filter with the specified sample rate and cutoff frequency
        lowPassFilter.setSampleRate(this.SampleRate);
        // Using default window length (64) and type ("Hamming") for simplicity
        lowPassFilter.setFilterParams((int) this.CutoffFrequency, 64, "Hamming");
        lowPassFilter.refreshComponent();

        // Ensure the output block length is correct
        resetBlockLength(blockLength);
    }

    // ================== Adaptive Rate Handling ==================

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // Adapt to changing block lengths from the input
        if (data.length != this.blockLength) {
            this.resetBlockLength(data.length);
            // Also update internal components
            this.nonLinearComponent.resetBlockLength(data.length);
            this.lowPassFilter.resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    // ================== Modify Properties Interface ==================
    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            if ("CarrierFrequency".equals(name)) {
                this.setCarrierFrequency(Double.parseDouble(value.toString()));
                return true;
            } else if ("CutoffFrequency".equals(name)) {
                this.setCutoffFrequency(Double.parseDouble(value.toString()));
                return true;
            }
            return super.ModifyPropertiesByName(name, value);
        } catch (Exception e) {
            System.err.println("Failed to modify property: " + name + ", Value: " + value + ". Error: " + e.getMessage());
            return false;
        }
    }


    // ================== Getters and Setters ==================

    public double getCarrierFrequency() {
        return CarrierFrequency;
    }

    public void setCarrierFrequency(double carrierFrequency) {
        if (carrierFrequency <= 0) {
            throw new IllegalArgumentException("Carrier frequency must be a positive number.");
        }
        this.CarrierFrequency = carrierFrequency;
        this.refreshComponent(); // No direct effect on calculation, but good practice
    }

    public double getCutoffFrequency() {
        return CutoffFrequency;
    }

    public void setCutoffFrequency(double cutoffFrequency) {
        if (cutoffFrequency <= 0 || (this.SampleRate > 0 && cutoffFrequency >= this.SampleRate / 2.0)) {
            throw new IllegalArgumentException("Cutoff frequency must be positive and less than the Nyquist frequency.");
        }
        this.CutoffFrequency = cutoffFrequency;
        this.refreshComponent(); // Apply the change to the internal LPF
    }
}