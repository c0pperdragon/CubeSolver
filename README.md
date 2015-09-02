# CubeSolver
Rubik's Cube solving robot based on the Lego Mindstorms NXT kit.

I wanted to build a cube solving robot for a long time. Many attempts failed due to 
my self-imposed restrictions to do it with just the hardware my Lego NXT kit provides: 
3 motors, 1 color sensor, CPU with about 100 KB flash memory and virtually no processing power.

I finally came up with a robot mechanism that allows pretty quick manipulation of
the cube along with a solver algorithm that fits on the hardware and does not 
perform too badly. 

The solver works with pre-computed static tables for every of the 7 phases I have broken
the solution down to. These phases loosely follow the approach from Lars Petrus.
The pre-computation for the petrusX.bin files was done with some java programs, I have included here
for the sake of completenes, but using them is not out-of-the box and needs heavy adjustment from the user.

There are a videos on Youtube showing two different variants of the machine:

	https://www.youtube.com/watch?v=rfwsLXOxM9Y
	
	https://www.youtube.com/watch?v=1ty5MxgaGak
	