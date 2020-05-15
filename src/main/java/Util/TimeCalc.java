package Util;

public class TimeCalc {
    public int millisecond;
    public int minutes;
    public int seconds;
    public int milliseconds;

    public int sec;
    public int mil;
    public int restx;

    public String rest;
    public String time;



    public int millisecondCalcInteger(String millisec) {

        String[] minSplit = millisec.split("\"");
        minutes = Integer.parseInt(minSplit[0]);
        rest = minSplit[1];

        String[] secSplit = rest.split("\'");
        seconds = Integer.parseInt(secSplit[0]);
        milliseconds = Integer.parseInt(secSplit[1]);
        //System.out.println(minuts +" " + seconds + " " + milliseconds);

        millisecond = (minutes *60000) + (seconds*1000) + (milliseconds);
        //System.out.println(millisecond);

        return millisecond;
    }


    public String millisecondCalcString(int millisec) {

        minutes = millisec/60000;
        restx = millisec%60000;
        sec = restx /1000;
        mil = (restx %1000)/10;

        time = minutes +"\"" + String.format("%02d", sec) +"'" + String.format("%02d",mil);

        return time;
    }


}

