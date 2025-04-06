package com.example.sdr.Core.Components.JythonTest;

import org.python.util.PythonInterpreter;

import com.example.sdr.Core.Components.Tools.GeneralResourceFinder;

import java.nio.file.Paths;

import org.python.core.*;

public class JythonTest {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter(); 

        GeneralResourceFinder finder = new GeneralResourceFinder();
        String pythonPath = finder.getFilePath("/matlab_python/AGCTest.py");

        interpreter.execfile(Paths.get(pythonPath).toAbsolutePath().toString());

        interpreter.close();
    }
}
