package cs310.team.project;

public class Badge {
           private String description;
	   private String id;
	
	   public Badge(){}
           
           
	   public Badge(String id, String description){
		this.description = description;
		this.id = id;
	}
	
	   public String getDescription() {return description;}
	   public String getId() {return id;}


	   public void setDescription(String description) { this.description = description;}
	   public void setId(String id) { this.id = id;}


	   @Override
	   public String toString(){
		return "#" + getId() + " (" + getDescription() + ")";
	}
}
