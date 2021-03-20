package es.alert21.atopcal.TOPO;
//Arctangent function
//Unless there is a note to the contrary, and if the expression for which the arctangent
// is sought has a numerator over a denominator,
// the formulas are arranged so that the atan2 function should be used.
// For atan(y/x), the arctangent is normally returned as an angle between -π/2 and +π/2
// (between -90° and +90°),
// whereas the atan2(y,x) output accounts for the quadrant resulting in output between -π and +π
// (-180° and +180°).
//Readers should note that using the atan2 function the order of the arguments for atan(y/x)
// varies across different programming languages and tools.
// This Guidance Note uses the convention atan2(y,x) for atan(y/x).
//Conditions not resolved by the atan2 function, but requiring adjustment for almost any program,
// are as follows:
//1) If x and y are both zero, the arctangent is indeterminate,
// but may normally be given an arbitrary value of 0, and
//2) If x or y is infinite, the arctangent is ±π/2 (±90°),
// the sign depending on other conditions.
// In any case, the final longitude should be adjusted,
// if necessary, so that it is an angle between -π (or -180°) and +π (or +180°).
// This is done by adding or subtracting multiples of 360° (or 2π) as required
// (see also longitude 'wrap-around' below).
public class Topo {
    public static double Azimut(double x1,double y1,double x2,double y2){
        double xx,yy;
        double az = 0.0;
        xx = x2-x1;
        yy = y2-y1;
        if (xx == 0.0 && yy == 0.0)
            return 0.0;
        if (yy == 0.0 && xx > 0.0)
            return 100.0;
        if (yy == 0.0 && xx < 0.0)
            return 300.0;
        az = Math.atan(xx/yy) * 200 / Math.PI;
        //az = Math.atan2(yy,xx) * 200 / Math.PI;
        if (az < 0.0) az += 400;
        return az;
    }
    public static double desorientacion(double obs1,double obs2){
        double des = obs1 - obs2;
        des = normaliza(des);
        if (des > 399)
            des = des - 400;
        return des;
    }
    public static double normaliza(double x){
        while (x < 0) x += 400;
        while (x >= 400) x -= 400;
        return x;
    }
}
