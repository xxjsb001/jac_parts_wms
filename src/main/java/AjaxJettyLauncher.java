
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;

public class AjaxJettyLauncher {
	protected int port = 8088;
	protected String contextName = "jac_parts_wms";
	protected String deployPath = "";
	
	public static void main(String[] args) throws Exception {
		AjaxJettyLauncher jl = new AjaxJettyLauncher();
		jl.init();
		jl.run();
	}
	
	protected void init() {
	}
	
	protected void run() throws Exception {
		Server server = new Server();
		SocketListener listener = new SocketListener();
		listener.setPort(getPort()); 
		server.addListener(listener);
		server.addWebApplication("/" + getContextName() , getDeployPath());
		server.start();
	}
	
	protected int getPort() {
		return port;
	}
	
	protected String getContextName() {
		return contextName;
	}
	
	protected String getDeployPath() {
		if (deployPath.length() == 0) {
			deployPath = "./target/" + contextName + "/";
		}
		return deployPath;
	}
}
