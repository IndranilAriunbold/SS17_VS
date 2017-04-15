package de.fh.darmstadt.vs;





public class Sensor {
	public Sensor(int id, String article)
	{
		articleName   = article;
		sensorId      = id;
		fillingDegree = 100; 
	}


	public void reduceFillingDegree(int level)
	{
		if (fillingDegree == 0)
			fillingDegree = 100;
		else if (fillingDegree > level)
			fillingDegree -= level;
		else 
			fillingDegree = 0;
	}



	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb. append("SensorId: ");
		sb.append(sensorId);
		sb. append(", Artikel: ");
		sb.append(articleName);
		sb. append(", Fuellgrad: ");
		sb.append(fillingDegree);
		return sb.toString();
	}


	private int    fillingDegree;
	private int    sensorId;
	private String articleName;
}

