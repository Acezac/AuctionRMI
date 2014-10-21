//bidder_cr includes the clients that have placed a bid in an auction

import java.util.Comparator;


public class bidder_cr{
	
private auctionclientserver bidder;
private double offer;
private int id;


bidder_cr( auctionclientserver bd, double o, int idd){
	bidder=bd;
	offer=o;
	id=idd;
}

//sort the bidders arraylist by offers
public static class CompId implements Comparator<bidder_cr> {
    public int compare(bidder_cr arg0, bidder_cr arg1) {
        return (int) (arg0.getoffer() - arg1.getoffer());
    }
}

public auctionclientserver getBidder(){
	return bidder;
}

public double getoffer(){
	return offer;
}

public int getId(){
	return id;
}

public void setBidder(auctionclientserver bidd){
	bidder=bidd;
}

public void setOffer(double of){
	offer=of;
}

public void setid(int id2){
	id=id2;
}


public String toString() {
    return id + " " + bidder + " " + offer;
}

}
