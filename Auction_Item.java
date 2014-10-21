//auction item
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;



public class Auction_Item implements Serializable {

private static final long serialVersionUID = -1754790187051148652L;
private String Item_name;
private int id;
private Calendar closing_date;
private int min_value;
private String flag;
private auctionclientserver owner;


//constructor of an auction
Auction_Item(int idd,String n,int min, Calendar c,String f,auctionclientserver o){
	id=idd;
	Item_name=n;
	min_value=min;
	closing_date=c;
	flag=f;
	owner=o;
}

//sort arraylist by minimum value
public static class CompMinValue implements Comparator<Auction_Item> {
	 
    public int compare(Auction_Item arg0, Auction_Item arg1) {
        return (int) (arg0.getMin_vlue() - arg1.getMin_vlue());
    }
}
//sort arraylist by the status of an item
public static class CompStatus implements Comparator<Auction_Item> {
	 
    public int compare(Auction_Item arg0, Auction_Item arg1) {
        return (int) (arg0.getFlag().compareTo(arg1.getFlag()));
    }
}
//sort an arraylist by the closing date
public static class CompClosingDate implements Comparator<Auction_Item> {
	 
    public int compare(Auction_Item arg0, Auction_Item arg1) {
        return (int) (arg0.getClosing_Date().getTimeInMillis() - arg1.getClosing_Date().getTimeInMillis());
    }
}

public int getID(){
	return id;
}

public String getFlag(){
	return flag;
}


public String getName(){
	return Item_name;
}

public Calendar getClosing_Date(){
	return closing_date;
}



public int getMin_vlue(){
	return min_value;
}

public auctionclientserver getOwner(){
	return owner;
}




public void setName(String nm){
	Item_name=nm;
}


public void setClosing_Date(Calendar cal){
	closing_date=cal;
}

public void setMin_vlaue(int minn){
	min_value=minn;
}

public void setID(int iddd){
	id=iddd;
}

public void setFlag(String fl){
	flag=fl;
}

public void setOwner(auctionclientserver ow){
	owner=ow;
}

// print the products in a specific format
public String toString() {
	
return String.format(("%-4s%-10s%-10s%-31s%-13s"), id, Item_name , flag , closing_date.getTime(),min_value+"\n"+"\n");

}

}
