package zpopeCalculateCosts;
import java.util.*;
import java.io.*;

public class CalculateProvisionCosts {

	public static void main(String[] args) {
		try {
			String fileName = "housingData.csv";
			FileInputStream housingFile = new FileInputStream(fileName);
			Scanner housingScanner = new Scanner(housingFile);
			String[] houseColumns = housingScanner.nextLine().split(",");
			
			int over60column = 0;
			int familyIncomeColumn = 0;
			int isDisabledColumn = 0;
			int hasChildrenColumn = 0;
			int internetColumn = 0;
			int electricBillColumn = 0;
			int foodStampColumn = 0;
			int medicareColumn = 0;
			int unemploymentColumn = 0;
			int rentColumn = 0;
			
			int[] provisionCosts = new int[11];
			
			for (int i = 0; i < houseColumns.length; i++) {
				if (houseColumns[i].equals("R60")) {
					over60column = i;
				}
				if (houseColumns[i].equals("FINCP")) {
					familyIncomeColumn = i;
				}
				if (houseColumns[i].equals("DIS")) {
					isDisabledColumn = i;
				}
				if (houseColumns[i].equals("NOC")) {
					hasChildrenColumn = i;
				}
				if (houseColumns[i].equals("ACCESS")) {
					internetColumn = i;
				}
				if (houseColumns[i].equals("ELEP")) {
					electricBillColumn = i;
				}
				if (houseColumns[i].equals("FS")) {
					foodStampColumn = i;
				}
				if (houseColumns[i].equals("HINS3")) {
					medicareColumn = i;
				}
				if (houseColumns[i].equals("NWAB")) {
					unemploymentColumn = i;
				}
				if (houseColumns[i].equals("RNTP")) {
					rentColumn = i;
				}
			}
			
			while (housingScanner.hasNextLine()) {
				String house = housingScanner.nextLine();
				String[] houseInfo = house.split(",");
				
				if (houseInfo[over60column].equals("1") || houseInfo[over60column].equals("2")) {
					//$500 if someone over 60 is in the household (high risk)
					provisionCosts[0] += 500;
				}
				
				if (isNumeric(houseInfo[familyIncomeColumn])) {
					int income = Integer.parseInt(houseInfo[familyIncomeColumn]);
					if (income <= 60000) {
						//$2400 stimulus if family makes under $60000
						provisionCosts[1] += 2400;
					}
				}
				else {
					//income is 0 - add stimulus and unemployment benefits
					provisionCosts[1] += 2400;
					provisionCosts[9] += 2000;
				}
				
				if (houseInfo[isDisabledColumn].equals("1")) {
					//$1500 if member of household is disabled
					provisionCosts[2] += 1500;
				}
				
				if (isNumeric(houseInfo[hasChildrenColumn])) {
					int numChildren = Integer.parseInt(houseInfo[hasChildrenColumn]);
					//$1000 per child
					provisionCosts[3] += numChildren * 1000;
					
					if (numChildren > 5) {
						//Extra money for having a lot of children
						provisionCosts[4] += 2000;
					}
				}
				
				if (houseInfo[internetColumn].equals("3")) {
					//$1500 if household has no internet - intended to establish internet
					provisionCosts[5] += 1500;
				}
				
				if (isNumeric(houseInfo[electricBillColumn])) {
					int electricBill = Integer.parseInt(houseInfo[electricBillColumn]);
					int income = 0;
					if (isNumeric(houseInfo[familyIncomeColumn])) {
						income = Integer.parseInt(houseInfo[familyIncomeColumn]);
					}
					
					if (electricBill >= 500 && income <= 60000) {
						//Assist families with high electric bill if below 60000 income
						provisionCosts[6] += electricBill;
					}
				}
				
				if (houseInfo[foodStampColumn].equals("1")) {
					//$500 if household receives foodstamps
					provisionCosts[7] += 500;
				}
				
				if (houseInfo[medicareColumn].equals("1")) {
					//$750 if household has medicare recipient
					provisionCosts[8] += 750;
				}
				
				if (isNumeric(houseInfo[rentColumn])) {
					int rent = Integer.parseInt(houseInfo[rentColumn]);
					int income = 0;
					if (isNumeric(houseInfo[familyIncomeColumn])) {
						income = Integer.parseInt(houseInfo[familyIncomeColumn]);
					}
					
					if (income <= 60000) {
						//Rent assistance if income below 60000 threshold
						//3 months rent
						provisionCosts[10] += 3 * rent;
					}
				}
			}
			
			for (int i = 0; i < provisionCosts.length; i++) {
				System.out.println("Cost of x" + (i + 1) + " is " + provisionCosts[i]);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("That file does not exist!");
		}
		catch (Exception e) {
			System.out.println("The following error occurred: " + e.getMessage());
		}
	}
	
	//Simple function to determine if a string is numeric
	//Code borrowed from https://www.baeldung.com/java-check-string-number
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
