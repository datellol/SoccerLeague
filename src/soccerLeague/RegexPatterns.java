package soccerLeague;

	public final class RegexPatterns {
		public final static String NUMBER_PATTERN = "^\\d+$";
		public final static String RESULT_PATTERN = "^([\\w\\s]+)\\s(\\d{1,3}),\\s([\\w\\s]+)\\s(\\d{1,3})$";		
	}
	
	/*
	//readfile
	/*
		static String readFirstLineFromFile(String path) throws IOException {
			try (FileReader fr = new FileReader(path);
     		BufferedReader br = new BufferedReader(fr)) {
    			return br.readLine();
			}
		}	
	*/
	