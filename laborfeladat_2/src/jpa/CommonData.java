package jpa;

public class CommonData {

	private static final String PERSISTENCE_UNIT_NAME = "jpa_name";
	private static final String DBASE_DIRECTORY = "c:\\BME\\itlab1\\iit1-JPA\\laborfeladat_2\\db";

	public static String getDir() {
		return DBASE_DIRECTORY;
	}
	public static String getUnit() {
		return PERSISTENCE_UNIT_NAME;
	}

}
