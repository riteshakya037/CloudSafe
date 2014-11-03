package Algo;
public class passwordStrength {

	public int checkPasswordStrength(String password) {
		int strengthPercentage=0;
		String[] partialRegexChecks = { 
				".*[a-z]+.*", // lower
				".*[A-Z]+.*", // upper
				".*[\\d]+.*", // digits
				".*[@#$%]+.*" // symbols
		};


		if (password.matches(partialRegexChecks[0])) {
			strengthPercentage+=20;
		}
		if (password.matches(partialRegexChecks[1])) {
			strengthPercentage+=20;
		}
		if (password.matches(partialRegexChecks[2])) {
			strengthPercentage+=20;
		}
		if (password.matches(partialRegexChecks[3])) {
			strengthPercentage+=20;
		}
		if(password.length()>=5){
			strengthPercentage+=10;
		}
		if(password.length()>=10){
			strengthPercentage+=10;
		}



		return strengthPercentage;
	}


}