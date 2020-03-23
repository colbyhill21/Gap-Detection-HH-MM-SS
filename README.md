# Gap-Detection-HHMMSS
Reads in a file of HH:MM:SS data points and determines if there are gaps in time. If so it ouputs how many there are, 
how long each gap is, average gap time, and finally the percentage of how much time was missed compared to the
time of recording.

## Getting Started
### Input
 - Filename to read the data from
 - Whether or not to use seconds or microseconds
### Output
  Prints out every gap in the time series at the beginning of where the gap was detected.
 
### Background
I wrote this program while working on a research project that required me to analyze upwards of 30 hours of data which
all included timestamps. I needed to analyze the time series and determine if there were any gaps in the data, 
this quickly lead me to write this script.
