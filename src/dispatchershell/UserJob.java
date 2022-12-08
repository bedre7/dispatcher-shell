package dispatchershell;

public class UserJob implements IUserJob{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distribute(IProcess process) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void distribute(IProcess process) {
		
		switch(process.getPriority()) {
		
		case HIGHESTPRIORITY:{
			
			break;
		}
		case MEDIUMPRIORITY:{
			
			break;
		}
		case LOWESTPRIORITY:{
			
			break;
		}
		default:
			break;
		
		}
	}
	
}
