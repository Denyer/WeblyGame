/*
 * use enum for the singleton factory pattern as Java will 
 * ensure there is only ever one instance
 * 
 * This pattern will return the current level
 */
public enum LevelFactory {
	
	INSTANCE;
	
	private final Level currentLevel;
	
	LevelFactory()
	{
		//final Level currentLevel;
		Level tempLevel;
		try
		{
			tempLevel = (Level) Class.forName("webly.game.Level"+ String.valueOf(Game.getChosenLevel()))
				                    .newInstance();
		}
		catch(Exception Exception)
		{
			System.out.println(Exception.getMessage());
			tempLevel = new Level1();
		}
		currentLevel = tempLevel;
	}
	
	public Level getLevel()
	{
		return currentLevel;
	}
	
}