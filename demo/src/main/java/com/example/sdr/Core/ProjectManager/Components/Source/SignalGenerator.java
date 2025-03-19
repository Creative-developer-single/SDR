package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SignalGenerator extends BaseComponent{
    private int blockLength;
    private int SampleRate;

    public final int SIGNAL_SIN = 0;
    public final int SIGNAL_COS = 1;
    public final int SIGNAL_SQUARE = 2;
    public final int SIGNAL_TRIANGLE = 3;
    public final int SIGNAL_SAWTOOTH = 4;
    public final int SIGNAL_WHITE_NOISE = 5;

    private double[] ans;
    private double frequency;
    private double amplitude;
    private double phase;

    private double blockPhase;

    private int signalType;

    public SignalGenerator(int blockLength,int SampleRate,String ID){
        super(blockLength,0);
        this.ID = ID;
        this.blockLength = blockLength;
        this.blockPhase = 0;
        this.SampleRate = SampleRate;

        ans = new double[blockLength];
    }

    public SignalGenerator(int blockLength, int signalType,double frequency, double amplitude, double phase){
        super(blockLength,0);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;

        ans = new double[blockLength];
    }

    public SignalGenerator(int blockLength, int signalType,double frequency, double amplitude, double phase,double SampleRate,String ID){
        super(blockLength,0,ID);
        this.blockLength = blockLength;
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
        this.SampleRate = (int)SampleRate;

        ans = new double[blockLength];
    }

    public double[] getAns(){
        return ans;
    }

    public void setSignalType(int signalType){
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
        if(signalType == SIGNAL_SIN){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.sin(this.blockPhase);
            }
        }else if(signalType == SIGNAL_COS){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.cos(this.blockPhase);
            }
        }else if(signalType == SIGNAL_SQUARE){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.signum(Math.sin(this.blockPhase));
            }
        }else if(signalType == SIGNAL_TRIANGLE){
            for(int i = 0; i < blockLength; i++){
                this.blockPhase = (this.blockPhase + 2 * Math.PI * frequency / SampleRate) % (2 * Math.PI);
                ans[i] = amplitude * Math.asin(Math.sin(this.blockPhase));
            }
        }else if(signalType == SIGNAL_WHITE_NOISE){
            for(int i = 0; i < blockLength; i++){
                ans[i] = amplitude * Math.random();
            }
        }
    }
}
