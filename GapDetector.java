import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;
public class GapDetector
{
	public static void main(String [] args)
	{
		
		System.out.println("Welcome to the Gap Detection Program");
		@SuppressWarnings("resource")
		Scanner kb = new Scanner(System.in);
		System.out.println("Enter the name of the file to read");
		String answer1 = kb.nextLine();
		String answer2 = "output.txt";
		boolean answer3 = true;
		System.out.println("Starting read");
		ArrayList<String> arr = new ArrayList<String>(); 
		arr = readF(answer1,answer2,answer3);
		for(int i=0;i<arr.size();i++)
		{
			System.out.println("Gap at "+arr.get(i));
		}
		
	}
	public static ArrayList<String> readF(String fileName, String outputName,boolean micro)
	{
		int numGaps = 0;
		double avgGapLength = 0.0;
		ArrayList<String> gapList = new ArrayList<String>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputName));
			String line;
			int lastHour = 0,lastMinute = 0, lastSecond = 0;
			boolean firstLine = true;
			int startHour = 0, startMinute = 0, startSecond = 0, endHour = 0, endMinute = 0, endSecond = 0;
			while((line = br.readLine())!=null)
			{
				String[] column = line.split(":");
				if(column.length > 3)
					break;
				if(firstLine)
				{
					lastHour = Integer.parseInt(column[0]);
					lastMinute = Integer.parseInt(column[1]);
					if(micro)
						lastSecond = Integer.parseInt(column[2]);
					bw.write(line+"\n");
					firstLine = false;
					startHour = Integer.parseInt(column[0]);
					startMinute = Integer.parseInt(column[1]);
					startSecond = Integer.parseInt(column[2]);
				}
				else //HH:MM:SS:
				{
					int currHour = Integer.parseInt(column[0]), currMinute = Integer.parseInt(column[1]), currSecond = Integer.parseInt(column[2]);
					endHour = currHour;
					endMinute = currMinute;
					endSecond = currSecond;
					if(currHour == lastHour) //hour is the same
					{
						if(lastMinute == currMinute) //minute is the same
						{
							if((lastSecond+1) == currSecond) //second increases
							{
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							}
							else //gap in seconds
							{
								int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
								avgGapLength += gap;
								bw.write("********* Seconds: " + gap + " *********\n");
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								numGaps++;
							}
						}
						else if((lastMinute+1) == currMinute) //minute increased
						{
							if(lastSecond == 59 && currSecond == 0) //if min increased sec should be zero
							{
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							}
							else //gap
							{
								int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
								avgGapLength += gap;
								bw.write("********* Seconds: " + gap + " *********\n");
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								numGaps++;
							}
						}
						else if(lastMinute == 59 && currMinute == 0)
						{
							if(lastSecond == 59 && currSecond == 0) //if min increased sec should be zero
							{
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							}
							else //gap
							{
								int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
								avgGapLength += gap;
								bw.write("********* Seconds: " + gap + " *********\n");
								bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
								numGaps++;
							}
						}
						else //gap in minutes
						{
							int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
							avgGapLength += gap;
							bw.write("********* Seconds: " + gap + " *********\n");
							bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							numGaps++;
						}
					} //end of LH = CH
					else if((lastHour+1) == currHour) //hour increased. Prev should be X:59:59
					{
						if(lastMinute == 59 && currMinute == 0 && lastSecond == 59 && currSecond == 0)
						{
							bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
						}
						else
						{
							int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
							avgGapLength += gap;
							bw.write("********* Seconds: " + gap + " *********\n");
							bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							numGaps++;
						}
					}
					else if(lastHour == 23 && currHour == 0) //hour increased through night. prev: 23:59:59
					{
						if(lastMinute == 59 && currMinute == 0 && lastSecond == 59 && currSecond == 0)
						{
							bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
						}
						else
						{
							int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
							avgGapLength += gap;
							bw.write("********* Seconds: " + gap + " *********\n");
							bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
							numGaps++;
						}
					}
					else //gap in hours, sheeesh
					{
						int gap = lengthOfGapInSeconds(lastHour,lastMinute,lastSecond,currHour,currMinute,currSecond);
						avgGapLength += gap;
						bw.write("********* Seconds: " + gap + " *********\n");
						bw.write(column[0] +":" + column[1] + ":"+ column[2] + "\n");
						gapList.add(column[0] +":" + column[1] + ":"+ column[2] + "\n");
						numGaps++;
					}
					
				}
				lastHour = Integer.parseInt(column[0]);
				lastMinute = Integer.parseInt(column[1]);
				lastSecond = Integer.parseInt(column[2]);
			} // end of the while.
			int runtimeInSec = lengthOfGapInSeconds(startHour,startMinute,startSecond,endHour,endMinute,endSecond);
			int logtime = runtimeInSec;
			int hour = runtimeInSec / 3600;
			runtimeInSec -= hour*3600;
			int min = runtimeInSec / 60;
			runtimeInSec -= min*60;
			
			avgGapLength /= numGaps;
			avgGapLength = round(avgGapLength,2);
			bw.write("Runtime: " + hour + " Hours " + min + " Minutes "  + runtimeInSec + " Seconds. " + "Number of Gaps: " + numGaps + ". Average Gap Length: "+avgGapLength+ " seconds\n");
			double comp =  ((logtime - avgGapLength*numGaps) / logtime);
			comp = round(comp,2);
			bw.write("Completeness of data: " + comp*100 + "%");
			System.out.println("finished");
			br.close();
			bw.close();
			
		}
		catch(IOException e) //Exception data
		{
			System.out.println("Houston we had a problem");
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("Houston we had a problem");
			System.out.println(e.getMessage());
		}
		return gapList;
	}
	public static int lengthOfGapInSeconds(int sH, int sM, int sS,int eH, int eM, int eS)
	{ 
		int gapLength = 0;
			if(sH == eH)
			{
				if(sM == eM) 
					gapLength = eS - sS;
				else
				{
					gapLength += (60*(eM-sM));
					if(eS != sS)
						gapLength += (eS-sS);
				}
			}
			else if(sH == 12 && eH == 1)
			{
				gapLength += 3600;
				if(sM != eM)
				{
					gapLength += (60*(eM-sM));
					if(eS != sS)
						gapLength += (eS-sS);
				}
			}
			else
			{
				gapLength += (60*60*(eH-sH)); 
				if(sM != eM)
				{
					gapLength += (60*(eM-sM));
					if(eS != sS)
						gapLength += (eS-sS);
				}
			}
		return gapLength;
	}
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
