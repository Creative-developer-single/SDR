import numpy as np
import matplotlib.pyplot as plt

# AGC Test
# Global Definitions
pi = 3.1415926

# AGC Test:5
fs = 10000  # Sampling frequency

# Generate test signal
f1 = 1000  # Frequency of the signal
t = np.arange(0, 0.1 - 1 / fs, 1 / fs)  # Time array for first signal
t2 = np.arange(0.1, 0.2, 1 / fs)  # Time array for second signal

# Create the test signal (s1)
s1 = np.concatenate([np.sin(2 * pi * f1 * t), 3 * np.sin(2 * pi * f1 * t2)])

# Generate AGC module
win_length = 64  # Window length
target_level = 0.5  # Target level for AGC
power_level = target_level / np.sqrt(2)  # Power level based on target level

# Initialize AGC variables
agc_in = np.concatenate([np.zeros(win_length), s1])
agc_out = []  # AGC output
agc_total = 0  # AGC total value
agc_mean = 0  # AGC mean value
agc_gain = 0  # AGC gain
max_gain = 20  # Maximum gain

# AGC processing loop
for m in range(len(s1)):
    agc_total = agc_total + abs(agc_in[m]) - abs(agc_in[m + win_length])
    agc_mean = agc_total / win_length
    agc_gain = min(max_gain, power_level / agc_mean)
    agc_out.append(agc_in[m + win_length] * agc_gain)

# Convert agc_out to a numpy array for further processing
agc_out = np.array(agc_out)

# Plot the outcome
# Plot the Source Signal
plt.figure(figsize=(10, 6))

# Plot the first subplot (source signal)
plt.subplot(2, 1, 1)
t_full = np.concatenate([t, t2])
plt.plot(t_full, s1)
plt.title('Source Signals')
plt.xlabel('t/s')
plt.ylabel('s1(t)')

# Plot the second subplot (AGC output)
plt.subplot(2, 1, 2)
plt.plot(t_full, s1, label='Source Signal')
plt.plot(t_full, agc_out, label='AGC Output')
plt.xlim([0.03, 0.1])
plt.ylim([-1, 1])
plt.title('AGC Output')
plt.xlabel('t/s')
plt.ylabel('A(t)')
plt.legend()

plt.tight_layout()
plt.show()
