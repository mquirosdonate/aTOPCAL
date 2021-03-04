package es.alert21.atopcal.OBS;

import static es.alert21.atopcal.Util.normaliza;

public class OBSx2 extends OBS{
    private OBS obs1;
    private OBS obs2;
    private OBS obsCD;
    private OBS obsCI;
    private boolean valid = true;
    public OBSx2(OBS obs1,OBS obs2){
        this.obs1 = obs1;
        this.obs2 = obs2;
        get_CD_CI();
    }

    public boolean getValid(){
        return this.valid;
    }
    public void setValid(boolean v){
        this.valid = v;
    }
    public OBS getObs1() {
        return obs1;
    }
    public OBS getObs2() {
        return obs2;
    }
    public OBS getObsCD(){
        return obsCD;
    }
    public OBS getObsCI(){
        return obsCI;
    }

    public double desorientacion(){
        double des = obs1.getH() - obs2.getH();
        des = normaliza(des);
        if (des > 399)
            des = des - 400;
        return des;
    }
    public double errorDistancia(){
        if (obs1.getD() == 0.0 || obs2.getD() == 0)
            return 0.0;
        return  obs1.getD() - (obs1.getD()+obs2.getD())*0.5;
    }
    public double errorHorizontal(){
        if (obsCD.getH() > obsCI.getH()){
            return (obsCD.getH()-(obsCI.getH()+200))*0.5;
        }else{
            return (obsCD.getH()-(obsCI.getH()-200))*0.5;
        }
    }
    public double errorVertical(){
        double x = obsCD.getV() + obsCI.getV() - 400;
        return x * 0.5;
    }
    public OBS obsCorregida (){
        OBS obs = new OBS(obsCD);
        obs.setH(obs.getH()-errorHorizontal());
        obs.setV(obs.getV()-errorVertical());
        obs.setD(obs.getD()-errorDistancia());
        return obs;
    }
    private void get_CD_CI(){
        if (obs1.isCD()){
            obsCD = obs1;
            obsCI = obs2;
        } else {
            obsCD = obs2;
            obsCI = obs1;
        }
    }
    public String toString(){
        String s = this.getObs1().toString() +
                "\n" +
                this .getObs2();
        return s;
    }
}
