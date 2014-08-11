package fw.connection.socks.interlude.gameserver.model;

/**
 * Created by Мы on 11.08.14.
 */
public class L2Charaster extends L2Object {

    public String name = "";
    public int curHp = 0;
    public int maxHp = 0;

    public int Heading = 34157;


    public int getPercentHP(){
        return curHp*100/maxHp;
    }
    public int getPercentHP(int cur){
        return cur*100/maxHp;
    }
    public int getPercentHPReduce(int cur){
        return getPercentHP() - getPercentHP(cur);
    }
}
