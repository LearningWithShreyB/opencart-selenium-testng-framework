package utilities;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name="LoginData")
	public String [][] getData() throws IOException
	{
		String path=".//src//test//resources//testdata//Opencart_LoginData.xlsx";
		
		ExcelUtility xlutil=new ExcelUtility(path);
		
		int totalrows=xlutil.getRowCount("Sheet1");	
		
		// FIX: Force the DataProvider to only read the first 3 columns 
		// (Index 0: Username, Index 1: Password, Index 2: res)
		int totalcols=3; 
				
		String logindata[][]=new String[totalrows][totalcols];
		
		for(int i=1;i<=totalrows;i++) 
		{		
			for(int j=0;j<totalcols;j++) 
			{
				logindata[i-1][j]= xlutil.getCellData("Sheet1",i, j); 
			}
		}
		return logindata;
	}
}