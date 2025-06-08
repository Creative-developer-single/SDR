package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SignalGenerator extends BaseComponent{
    private int blockLength;

    private double frequency;
    private double amplitude;
    private double phase;

    private double blockPhase;

    private String signalType;

    public SignalGenerator(int blockLength,int inputCount,int outputCount,String iD){
        super(blockLength, inputCount, outputCount,iD);
        this.blockLength = blockLength;
        this.blockPhase = 0;
        this.signalType = "Sine";
        this.Type = "Driver";

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }

    public SignalGenerator(int blockLength,int SampleRate,String ID){
        super(blockLength,1,1);
        this.ID = ID;
        this.blockLength = blockLength;
        this.blockPhase = 0;
        this.SampleRate = SampleRate;
        this.signalType = "Sine";
        this.Type = "Driver";

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }

    public SignalGenerator(int blockLength, String signalType,double frequency, double amplitude, double phase){
        super(blockLength,1,1);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.signalType = "Sine";

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }

    public SignalGenerator(int blockLength, String signalType,double frequency, double amplitude, double phase,double SampleRate,String ID){
        super(blockLength,0,1,ID);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.SampleRate = (int)SampleRate;
        this.signalType = "Sine";

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }


    public void setSignalType(String signalType){
        this.signalType = signalType;
    }

    public void setFrequency(double frequency){
        this.frequency = frequency;
    }

    public void setAmplitude(double amplitude){
        this.amplitude = amplitude;
    }

    public void setPhase(double phase){
        this.phase = phase;
    }

    @Override
    public void refreshComponent() {
        // Reset output array length to blockLength
        resetBlockLength(blockLength);
        this.blockPhase = 0;
    }

    public void Calculate(){
        GenerateSignal();
    }

    public void GenerateSignal(){
        if(signalType.equals("Sine")){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[0][i].fromDouble(amplitude * Math.sin(this.blockPhase));
            }
            System.out.println("Block Phase: " + this.blockPhase);
        }else if(signalType == "Cos"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[0][i].fromDouble(amplitude * Math.cos(this.blockPhase));
            }
        }else if(signalType == "Square"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[0][i].fromDouble(amplitude * Math.signum(Math.sin(this.blockPhase)));
            }
        }else if(signalType == "Triangle"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[0][i].fromDouble(amplitude * Math.asin(Math.sin(this.blockPhase)));
            }
        }else if(signalType == "Noise"){
            for(int i = 0; i < blockLength; i++){
                ans[0][i].fromDouble(amplitude * Math.random());
            }
        }
    }
}
