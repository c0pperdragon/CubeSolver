// fine-tuning points for the cube

// -- VALUES FOR MY SPEED CUBE ULTIMATE 2
//#define overshoot_A         1
//#define overshoot_A2        -11
//#define angle_twistb2       35        // relative to idle (finish twist)
// -- VALUES FOR MY ORIGINAL RUBIKS CUBE
#define overshoot_A           5        // additional angle for 1/4 turn of bottom
#define overshoot_A2          -3       // additional angle for 1/2 turn of bottom
#define angle_twistb2         40       // relative to idle (finish twist)


// motor control module for the cube solver
long start_angle[3];

#define base_A               28


#define angle_open         (-10)
#define angle_close        (-70)

#define angle_twistcenter (-74)         // idle position of fork roation
#define angle_twistb1     (-65)         // relative to idle (start twist)

// logic face turn actions
#define FACETURN_D   0
#define FACETURN_D2  1
#define FACETURN_Di  2
#define FACETURN_L   3
#define FACETURN_L2  4
#define FACETURN_Li  5
#define FACETURN_R   6
#define FACETURN_R2  7
#define FACETURN_Ri  8
#define FACETURN_F   9
#define FACETURN_F2  10
#define FACETURN_Fi  11
#define FACETURN_B   12
#define FACETURN_B2  13
#define FACETURN_Bi  14

const bool isclockwise[] = {
  true, true, false,  true,true,false, true,true,false, true,true,false, true,true,false
};
const byte axis[] = { 0,0,0,  1,1,1,1,1,1, 2,2,2,2,2,2 };

int cubeorientation;
int scan_hues[8];

const byte inverted[] = {
      FACETURN_Di, FACETURN_D2, FACETURN_D,
      FACETURN_Li, FACETURN_L2, FACETURN_L,
      FACETURN_Ri, FACETURN_R2, FACETURN_R,
      FACETURN_Fi, FACETURN_F2, FACETURN_F,
      FACETURN_Bi, FACETURN_B2, FACETURN_B
};
const byte rotated[][] =
{
    { FACETURN_D, FACETURN_D2, FACETURN_Di,
      FACETURN_L, FACETURN_L2, FACETURN_Li,
      FACETURN_R, FACETURN_R2, FACETURN_Ri,
      FACETURN_F, FACETURN_F2, FACETURN_Fi,
      FACETURN_B, FACETURN_B2, FACETURN_Bi
    },
    { FACETURN_D, FACETURN_D2, FACETURN_Di,
      FACETURN_F, FACETURN_F2, FACETURN_Fi,
      FACETURN_B, FACETURN_B2, FACETURN_Bi,
      FACETURN_R, FACETURN_R2, FACETURN_Ri,
      FACETURN_L, FACETURN_L2, FACETURN_Li
    },
    { FACETURN_D, FACETURN_D2, FACETURN_Di,
      FACETURN_R, FACETURN_R2, FACETURN_Ri,
      FACETURN_L, FACETURN_L2, FACETURN_Li,
      FACETURN_B, FACETURN_B2, FACETURN_Bi,
      FACETURN_F, FACETURN_F2, FACETURN_Fi
    },
    { FACETURN_D, FACETURN_D2, FACETURN_Di,
      FACETURN_B, FACETURN_B2, FACETURN_Bi,
      FACETURN_F, FACETURN_F2, FACETURN_Fi,
      FACETURN_L, FACETURN_L2, FACETURN_Li,
      FACETURN_R, FACETURN_R2, FACETURN_Ri
    }
};
const byte mirrored[] = {
      FACETURN_Di, FACETURN_D2, FACETURN_D,
      FACETURN_Ri, FACETURN_R2, FACETURN_R,
      FACETURN_Li, FACETURN_L2, FACETURN_L,
      FACETURN_Fi, FACETURN_F2, FACETURN_F,
      FACETURN_Bi, FACETURN_B2, FACETURN_B
};

long getAngle(byte m)
{
    return MotorRotationCount(m) - start_angle[m];
}


long roundAto90deg()
{
     long a = getAngle(OUT_A) - base_A;

     if (a>=0) a =   ((a+45)/90)*90;
     else      a = -(((45-a)/90)*90);

     return a + base_A;
}

void waitforendposition(byte o, long pos, int maxdelta)
{
     long st = CurrentTick();
     while (CurrentTick() < st+2000)
     {
         long delta = pos - getAngle(o);
         if (delta>=-maxdelta && delta<=maxdelta) return;
     }
}

void waitforstandstill(byte o, int resttime)
{
     long st = CurrentTick();
     long prev = MotorRotationCount(o);
     long timeofchange = CurrentTick();
     while (CurrentTick() < timeofchange + resttime)
     {   long current = MotorRotationCount(o);
         if (current!=prev)
         {  prev = current;
            timeofchange = CurrentTick();
         }
         Wait(1);
     }
}

void PosRegSetAngleAndWait(byte o, long pos, int maxdelta)
{
     PosRegSetAngle(o, pos);
     waitforendposition(o,pos,maxdelta);
}


void TWIST_90CW_A(int overshoot)
{
         PosRegSetAngleAndWait  (OUT_B, angle_twistcenter,30);
         PosRegSetAngleAndWait  (OUT_C, angle_close, 3);
         PosRegSetAngleAndWait  (OUT_A, roundAto90deg()+90+overshoot_A+overshoot, 1);
         PosRegSetAngle (OUT_A, roundAto90deg());
         waitforstandstill(OUT_A, 10);
         PosRegSetAngle  (OUT_C, angle_open);
}

void TWIST_90CCW_A(int overshoot)
{
          PosRegSetAngleAndWait  (OUT_B, angle_twistcenter,30);
          PosRegSetAngleAndWait  (OUT_C, angle_close, 3);
          PosRegSetAngleAndWait  (OUT_A, roundAto90deg()-90-overshoot_A-overshoot, 1);
          PosRegSetAngle (OUT_A, roundAto90deg());
          waitforstandstill(OUT_A, 10);
          PosRegSetAngle (OUT_C, angle_open);
}

void TWIST_180CW_A(int overshoot)
{
          PosRegSetAngleAndWait  (OUT_B, angle_twistcenter,30);
          PosRegSetAngleAndWait  (OUT_C, angle_close, 3);
          PosRegSetAngleAndWait  (OUT_A, roundAto90deg()+2*90+overshoot_A2+overshoot, 1);
          PosRegSetAngle (OUT_A, roundAto90deg());
          waitforstandstill(OUT_A, 10);
          PosRegSetAngle (OUT_C, angle_open);
}

void TWIST_90CW_B(int pre_turn_a, int overshoot)
{
    long targeta = roundAto90deg();
    int evasive=0;
    int proximity=0;

    switch (pre_turn_a)
    {   case 0:
            evasive = 15;
            proximity = 10;
            break;
        case 3:   // clockwise 90  -> can move other motor nearly immediately
            targeta += 90;
            evasive = 10;
            proximity = 70;
            break;
        case 2:   // couter-clockwise 180  -> can move other after some time
            targeta -= 180;
            evasive = 20;
            proximity = 70;
            break;
        case 1:   // couter-clockwise 90  -> can move other motor immediately
            targeta -= 90;
            evasive = 17;
            proximity = 30;
            break;
     }

     PosRegSetAngleAndWait  (OUT_C, angle_open, 40);
     PosRegSetAngleAndWait  (OUT_A, targeta+evasive, proximity);
     PosRegSetAngleAndWait  (OUT_B, angle_twistcenter-angle_twistb1,  10);
     PosRegSetAngleAndWait  (OUT_C, angle_close, 10);
     PosRegSetAngle (OUT_A, targeta);
     PosRegSetAngleAndWait  (OUT_C, angle_close, 5);
     PosRegSetAngleAndWait  (OUT_B, angle_twistcenter-angle_twistb2-overshoot,  1);
     PosRegSetAngle  (OUT_B, angle_twistcenter);
     PosRegSetAngle  (OUT_C, angle_open);
}

void TWIST_90CCW_B(int pre_turn_a, int overshoot)
{
    long targeta = roundAto90deg();
    int evasive=0;
    int proximity = 0;

    switch (pre_turn_a)
    {   case 0:
            evasive = -15;
            proximity = 10;
            break;
        case 3:   // clockwise 90  -> can move other after some time
            targeta += 90;
            evasive = -17;
            proximity = 30;
            break;
        case 2:   // couter-clockwise 180  -> can move other after some time
            targeta += 180;
            evasive = -20;
            proximity = 70;
            break;
        case 1:   // couter-clockwise 90  -> can move other motor nearly immediately
            targeta -= 90;
            evasive = -10;
            proximity = 70;
            break;
     }

     PosRegSetAngleAndWait  (OUT_C, angle_open, 40);
     PosRegSetAngleAndWait  (OUT_A, targeta+evasive, proximity);
     PosRegSetAngleAndWait  (OUT_B, angle_twistcenter+angle_twistb1, 10);
     PosRegSetAngleAndWait  (OUT_C, angle_close, 10);
     PosRegSetAngle (OUT_A, targeta);
     PosRegSetAngleAndWait  (OUT_C, angle_close, 5);
     PosRegSetAngleAndWait  (OUT_B, angle_twistcenter+angle_twistb2+overshoot, 1);
     PosRegSetAngle(OUT_B, angle_twistcenter);
     PosRegSetAngle (OUT_C, angle_open);
}


void turn_on_scanner()
{
    SetSensorColorFull(S1);
}

void turn_off_scanner()
{
    SetSensorType(S1, SENSOR_TYPE_NONE);
}

void scan_top_face()
{
     int colorval;
     unsigned int raw[4];
     unsigned int norm[4];
     int scaled[4];

     long base =  roundAto90deg();
     long target = base - 5*90;

     PosRegSetAngleAndWait (OUT_C, angle_open, 40);
     PosRegSetAngle(OUT_A, target);

     for (int i=0; i<8; i++)
     {
         long r = 0;
         long g = 0;
         long b = 0;
         long blank = 0;
         long count=0;
         
         long samplepoint = base - (i+2)*(90/2) - 5;

         while (getAngle(OUT_A) > samplepoint + 5);
         do
         {
              ReadSensorColorEx(
                S1,
                colorval,
                raw,
                norm,
                scaled);
              r += scaled[0];
              g += scaled[1];
              b += scaled[2];

              count++;
         }
         while (getAngle(OUT_A) >= samplepoint - 5)

        int R = r / count;
        int G = g / count;
        int B = b / count;
        
        int m = R;
        if (G<m) m=G;
        if (B<m) m=B;
        int M = R;
        if (G>M) M=G;
        if (B>M) M=B;

        int C = M-m;

        if (C==0 || (M>8*C && m>100))         // whitenes is overwhelming
        {   scan_hues[(i+4)%8] = -1;
        }
        else
        {   int H = 0;
            if (M==R)
            {  H = ((G-B)*60)/C + 360;
            }
            else if (M==G)
            {  H = ((B-R)*60)/C + 120;
            }
            else
            {  H = ((R-G)*60)/C + 240;
            }
            scan_hues[(i+4)%8] = H % 360;
        }
     }

     waitforendposition(OUT_A, target, 20);

     cubeorientation = (cubeorientation+1) % 4;
}
     
     
int orientcube(int o)
{
   int delta = (o + 4 - cubeorientation) % 4;
   cubeorientation = o;
   return delta;
}



void faceturn3(byte t, byte nextturn, byte nextturn2)
{
     int overshoot_for_cw_a;
     int overshoot_for_cw_lr;
     int overshoot_for_cw_fb;

     // for the cases whre t and nextxturn are on the same axis
     // e.g. twist F and then B, use the turn after that for decission
     if (axis[t]==axis[nextturn])
     {   nextturn = nextturn2;
     }

     // calculate from the nextturn info an overshoot
     // value for the current twist
     if (nextturn==FACETURN_D || nextturn==FACETURN_D2 || nextturn==FACETURN_Di)
     {    overshoot_for_cw_a = 0;
          overshoot_for_cw_lr = isclockwise[nextturn] ? 2: -2;
          overshoot_for_cw_fb = overshoot_for_cw_lr;
     }
     else if (nextturn==FACETURN_L || nextturn==FACETURN_L2 || nextturn==FACETURN_Li
           || nextturn==FACETURN_R || nextturn==FACETURN_R2 || nextturn==FACETURN_Ri)
     {    overshoot_for_cw_a = isclockwise[nextturn] ? 3 : -3;
          overshoot_for_cw_lr = 0;
          overshoot_for_cw_fb = isclockwise[nextturn] ? 2: -2;
     }
     else
     {    overshoot_for_cw_a = isclockwise[nextturn] ? 3 : -3;
          overshoot_for_cw_lr = isclockwise[nextturn] ? 2 : -2;
          overshoot_for_cw_fb = 0;
     }

     switch (t)
     {
     case FACETURN_D:
         TWIST_90CW_A(overshoot_for_cw_a);
         break;
     case FACETURN_D2:
         TWIST_180CW_A(overshoot_for_cw_a);
         break;
     case FACETURN_Di:
         TWIST_90CCW_A(-overshoot_for_cw_a);
         break;

     case FACETURN_L:
         TWIST_90CW_B(orientcube(2), overshoot_for_cw_lr);
         break;
     case FACETURN_L2:
         TWIST_90CW_B(orientcube(2), 0);
         TWIST_90CW_B(0, overshoot_for_cw_lr);
         break;
     case FACETURN_Li:
         TWIST_90CCW_B(orientcube(2), -overshoot_for_cw_lr);
         break;

     case FACETURN_R:
         TWIST_90CW_B(orientcube(0), overshoot_for_cw_lr);
         break;
     case FACETURN_R2:
         TWIST_90CW_B(orientcube(0), 0);
         TWIST_90CW_B(0, overshoot_for_cw_lr);
         break;
     case FACETURN_Ri:
         TWIST_90CCW_B(orientcube(0), -overshoot_for_cw_lr);
         break;

     case FACETURN_F:
         TWIST_90CW_B(orientcube(3), overshoot_for_cw_fb);
         break;
     case FACETURN_F2:
         TWIST_90CW_B(orientcube(3), 0);
         TWIST_90CW_B(0, overshoot_for_cw_fb);
         break;
     case FACETURN_Fi:
         TWIST_90CCW_B(orientcube(3), -overshoot_for_cw_fb);
         break;

     case FACETURN_B:
         TWIST_90CW_B(orientcube(1), overshoot_for_cw_fb);
         break;
     case FACETURN_B2:
         TWIST_90CW_B(orientcube(1), 0);
         TWIST_90CW_B(0, overshoot_for_cw_fb);
         break;
     case FACETURN_Bi:
         TWIST_90CCW_B(orientcube(1), -overshoot_for_cw_fb);
         break;
     }
}
void faceturn(byte t)
{
    faceturn3(t,t,t);
}


void init_motors()
{
   // move motors to reach mechanical limits for calibration
   OnFwd(OUT_C, 20);
   Wait(100);
   waitforstandstill(OUT_C, 100);
   OnFwd(OUT_B, 20);
   Wait(100);
   waitforstandstill(OUT_B, 100);
   OnFwd(OUT_A, 20);
   Wait(100);
   waitforstandstill(OUT_A, 100);

   // turn on position regulation for motors
   Off(OUT_A);
   Off(OUT_C);
   Off(OUT_B);
   PosRegEnable (OUT_A);
   PosRegEnable (OUT_B);
   PosRegEnable (OUT_C);
   PosRegSetMax(OUT_A, 0, 0);
   PosRegSetMax(OUT_B, 0, 0);
   PosRegSetMax(OUT_C, 0, 0);
   start_angle[OUT_A] = MotorRotationCount(OUT_A);
   start_angle[OUT_B] = MotorRotationCount(OUT_B);
   start_angle[OUT_C] = MotorRotationCount(OUT_C);
   SetMotorRegulationTime (10);

   // move back motors to neutral position
   PosRegSetAngle(OUT_A, -90+base_A);
   PosRegSetAngle(OUT_C, angle_open);
   PosRegSetAngleAndWait(OUT_B, angle_twistcenter, 10);

   cubeorientation=0;
}

