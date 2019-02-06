package CompilerRuntime;

import java.util.ArrayList;
import java.util.Collection;
import org.coreasm.engine.absstorage.Update;

/**
 * A list of updates
 * @author Markus Brenner
 *
 */
public class UpdateList extends ArrayList<Update>{
	private static final long serialVersionUID = 1L;
	
	public UpdateList(){
		super();
	}
	
	public UpdateList(Collection<Update> set) {
		super(set);
	}
	
	public UpdateList(Update u){
		super();
		this.add(u);
	}

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder("UpdateList\n");
		for (Update update : this) {
			s.append("(").append(update.loc).append(", ").append(update.action).append(", ").append(update.value).append(")\n");
		}
		return s.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof UpdateList){
			UpdateList ul = (UpdateList)o;
			if(ul.size() == this.size()){
				for(int i = 0; i < this.size(); i++){
					if(!this.get(i).equals(ul.get(i))) return false;
				}
				return true;
			}
		}
		return false;
	}
}
