package es.alert21.atopcal.OBS;

public class OBSx2 extends OBS{
    private OBS obs1;
    private OBS obs2;
    private OBS obsCD;
    private OBS obsCI;
    public OBSx2(OBS obs1,OBS obs2){
        this.obs1 = obs1;
        this.obs2 = obs2;
    }
    public OBSx2(){}

    public void setObs1(OBS obs1) {
        this.obs1 = obs1;
    }

    public void setObs2(OBS obs2) {
        this.obs2 = obs2;
    }
    public OBS getObsCD(){
        get_CD_CI();
        return obsCD;
    }
    public OBS getObsCI(){
        get_CD_CI();
        return obsCI;
    }

    public Double desorientacion(){
        Double des = obs1.getH() - obs2.getH();
        return normaliza(des);
    }
    public Double errorHorizontal(){
        get_CD_CI();
        Double x = obsCD.getH() - obsCI.getH();
        if (x < 0)
            x += 200;
        return x * 0.5;
    }
    public Double errorVertical(){
        get_CD_CI();
        Double x = obsCD.getV() + obsCI.getV() - 400;
        return x * 0.5;
    }
    public OBS obsCorregida (){
        get_CD_CI();
        OBS obs = obsCD;
        obs.setH(obs.getH()-errorHorizontal());
        obs.setV(obs.getV()-errorVertical());
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
}
