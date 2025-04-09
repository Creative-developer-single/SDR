package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SignalGenerator extends BaseComponent{
    private int blockLength;

    private double[] ans;
    private double frequency;
    private double amplitude;
    private double phase;

    private double blockPhase;

    private String signalType;

    public SignalGenerator(int blockLength,int sampleRate,String ID){
        super(blockLength,0);
        this.ID = ID;
        this.blockLength = blockLength;
        this.blockPhase = 0;
        this.sampleRate = sampleRate;

        ans = new double[blockLength];
    }

    public SignalGenerator(int blockLength, String signalType,double frequency, double amplitude, double phase){
        super(blockLength,0);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;

        ans = new double[blockLength];
    }

    public SignalGenerator(int blockLength, String signalType,double frequency, double amplitude, double phase,double sampleRate,String ID){
        super(blockLength,0,ID);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.sampleRate = (int)sampleRate;

        ans = new double[blockLength];
    }

    public double[] getAns(){
        return ans;
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

    public void resetBlockPhase(double phase){
        this.blockPhase = phase;
    }

    public void Calculate(){
        GenerateSignal();
    }

    public void GenerateSignal(){
        if(signalType.equals("Sine")){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / sampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.sin(this.blockPhase);
            }
        }else if(signalType == "Cos"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / sampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.cos(this.blockPhase);
            }
        }else if(signalType == "Square"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / sampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.signum(Math.sin(this.blockPhase));
            }
        }else if(signalType == "Triangle"){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / sampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.asin(Math.sin(this.blockPhase));
            }
        }else if(signalType == "Noise"){
            for(int i = 0; i < blockLength; i++){
                ans[i] = amplitude * Math.random();
            }
        }
    }
}
