package com.amap.map3d.tools;


/**
 * Created by ppssyyy on 2017-03-29.
 */
public class ExpUtil {
    public static final int MAX_EXP = 46562800;
    //100+200*(1+2+3)+500*(4+5+6)+1000*(7+8+9)+10000*(10+11+12)+
    //50000*(13+14+15)+100000*(16+17+18)+1000000*(19+20);

    /**
     * 每一级的升级最大显示经验(经验条最大经验)
     *
     * @param level
     * @return
     */
    public static int getMaxExpByLevel(int level) {
        switch (level) {
            case 1:
            case 2:
            case 3:
                return level * 200;
            case 4:
            case 5:
            case 6:
                return level * 500;
            case 7:
            case 8:
            case 9:
                return level * 1000;
            case 10:
            case 11:
            case 12:
                return level * 10000;
            case 13:
            case 14:
            case 15:
                return level * 50000;
            case 16:
            case 17:
            case 18:
                return level * 100000;
            case 19:
            case 20:
                return level * 1000000;
            default:
                return 100;
        }

    }

    /**
     * 每一级的升级最大累积经验（数据库升级经验）
     *
     * @param level
     * @return
     */
    public static int getMaxAddedExpByLevel(int level) {
        switch (level) {
            case 1:
            case 2:
            case 3:
                int a = 0;
                for (int i = 1; i <= level; i++) {
                    a += getMaxExpByLevel(i);
                }
                return a + 100;
            case 4:
            case 5:
            case 6:
                int b = 0;
                for (int i = 1; i <= level; i++) {
                    b += getMaxExpByLevel(i);
                }
                return b + 100;
            case 7:
            case 8:
            case 9:
                int c = 0;
                for (int i = 1; i <= level; i++) {
                    c += getMaxExpByLevel(i);
                }
                return c + 100;
            case 10:
            case 11:
            case 12:
                int d = 0;
                for (int i = 1; i <= level; i++) {
                    d += getMaxExpByLevel(i);
                }
                return d + 100;
            case 13:
            case 14:
            case 15:
                int e = 0;
                for (int i = 1; i <= level; i++) {
                    e += getMaxExpByLevel(i);
                }
                return e + 100;
            case 16:
            case 17:
            case 18:
                int f = 0;
                for (int i = 1; i <= level; i++) {
                    f += getMaxExpByLevel(i);
                }
                return f + 100;
            case 19:
            case 20:
                int g = 0;
                for (int i = 1; i <= level; i++) {
                    g += getMaxExpByLevel(i);

                }
                return g + 100;
            default:
                return 100;
        }

    }

    //根据用户数据库经验及等级获取当前显示条经验
    public static int getCurrentBarExpByUserExp(int userExp, int level) {
        //userExp-当前级别前一级的最大累积经验
        return level > 0 ? userExp - getMaxAddedExpByLevel(level - 1) : userExp;
    }

    /**
     * 增加经验后目前几级
     *
     * @param addExp  增加的经验
     * @param userExp 当前总经验
     * @param level   当前等级
     * @return
     */
    public static int getLevelByAddExp(int addExp, int userExp, int level) {
        int currentExp = addExp + userExp;//当前经验
        int upToLevel = level + 1; //可能会上升一级
        int exp = getMaxAddedExpByLevel(upToLevel - 1); //升这一级所需要的经验
        while (currentExp >= exp) { //如果当前经验比升一级的经验还多，继续升级
            upToLevel++;
            exp = getMaxAddedExpByLevel(upToLevel - 1);
        }
        return upToLevel - 1;
    }

    public static int stepsToExp(int stepsA, int stepsB, int status) {
        if (status == 1)
            return stepsA;
        else if (status == 2) {
            return (int) ((stepsA + stepsB) * ratio(stepsA, stepsB));
        } else return -1;
    }


    /**
     * 距下次升级还有多少经验
     *
     * @param level
     * @return
     */
    public static int expToNextLeve(int level, int currentExp) {
        return getMaxAddedExpByLevel(level) - currentExp;
    }

    /**
     * 返回折算系数
     *
     * @param stepA
     * @param stepB
     * @return
     */
    public static double ratio(int stepA, int stepB) {

        if (stepA != -1 && stepB != -1) {
            float k = stepA > stepB ? (float) stepB / stepA : (float) stepA / stepB;
            if (0 < k && k <= 0.1)
                return 0.1;
            else if (0.1 < k && k <= 0.4)
                return 0.3;
            else if (0.4 < k && k <= 0.6)
                return 0.5;
            else if (0.6 < k && k <= 0.9)
                return 0.7;
            else
                return 1;
        }else return 0;


    }
}
