import numpy as np
from scipy.signal import firwin, lfilter

def EnvelopeDetector(x, window_length, fs, fl, fh):
    # Take the absolute value of the signal
    x_abs = np.abs(x)

    # Design the FIR bandpass filter
    nyquist = 0.5 * fs
    low = fl / nyquist
    high = fh / nyquist
    taps = firwin(window_length, [low, high], pass_zero=False, window='hamming')

    # Apply the filter to the signal
    y = lfilter(taps, 1.0, x_abs)

    # Adjust the length of the output signal
    y = y[:len(y) - window_length + 1]

    return y

    