import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import firwin, lfilter

def SimpleFIRBandPassFilter(x, window_length, fl, fh, fs, window_type='Hamming'):
    nyquist = 0.5 * fs
    low = fl / nyquist
    high = fh / nyquist

    # Design the FIR filter
    taps = firwin(window_length, [low, high], pass_zero=False, window=window_type)

    # Apply the filter
    y = lfilter(taps, 1.0, x)
    
    return y

# Global Definitions
pi = 3.1415926
fs = 50000

# Generate the t vector
t = np.arange(0, 0.1, 1/fs)

# Generate test Signals
f1 = 1200
fc = 8000
a = 0.6
s1 = np.sin(2 * pi * f1 * t)
s = (1 + a * s1) * np.cos(2 * pi * fc * t)

# Abs the Signals
s_abs = np.abs(s)

# Apply the Filter
f_low = 500
f_high = 1500
window_length = 128

y = SimpleFIRBandPassFilter(s_abs, window_length, f_low, f_high, fs, window_type='hamming')

# Plot the Outcome
# Plot the AM Signals
plt.figure(figsize=(10, 8))

plt.subplot(3, 1, 1)
plt.plot(t, s1)
plt.title("Source Signals")
plt.xlabel("t/s")
plt.ylabel("s1(t)")

# Plot the AM Signals
plt.subplot(3, 1, 2)
plt.plot(t, s)
plt.title("AM Signals")
plt.xlabel("t/s")
plt.ylabel("s(t)")

# Plot the DeMod Signals
plt.subplot(3, 1, 3)
plt.plot(t, s1, label="Source")
plt.plot(t, y[:len(t)], label="Demodulated")
plt.title("DeMod Signals")
plt.xlabel("t/s")
plt.ylabel("y(t)")
plt.legend()

# Show the plot
plt.tight_layout()
plt.show()
