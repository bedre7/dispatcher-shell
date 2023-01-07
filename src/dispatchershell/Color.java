package dispatchershell;

//Proseslerinin renklerini temsil eden enum
public enum Color {
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	PURPLE("\u001B[35m"),
	CYAN("\u001B[36m"),
	WHITE("\u001B[37m"),
	RESET("\u001B[0m");
	
	public final String colorCode;
    
    //Constructor
	Color(String colorCode) {
        this.colorCode = colorCode;
    }
	
    public String getColor(){
        return this.colorCode;
    }
}
