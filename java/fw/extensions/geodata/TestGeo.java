package fw.extensions.geodata;

import java.util.ArrayList;
import java.util.logging.Logger;

import xmlex.config.ConfigSystem;
import fw.extensions.MemoryWatchDog;
import fw.extensions.util.Location;

public class TestGeo {
	
	private static Logger _log = Logger.getLogger(TestGeo.class.getName());
	
	public static void main(String[] agrs) {
		ConfigSystem.load();
		_log.info("start test....");
		_log.info("Memory: "+MemoryWatchDog.getMemUsedMb());		
		PathFindBuffers.initBuffers("8x100;8x128;8x192;4x256;2x320;2x384;1x500");
		GeoEngine.loadGeo();
		_log.info("Memory: "+MemoryWatchDog.getMemUsedMb());
		
		
		Location begin = new Location(-93465, 237917, -3510);
		Location end = new Location(-93153, 238055, -3553);
		
		ArrayList<Location> _path = GeoMove.findPath(begin.x, begin.y, begin.z, end, null, false, 0);
		
		for (Location loc: _path) {
			_log.info("Loc: "+loc.toString());
		}
		
		
		/*PathFind _pf = new PathFind(-93465, 237917, -3510,
				-93153, 238055, -3553, 
				null, 
				0);
		_log.info("Path...");
		for (Location loc: _pf.getPath()) {
			_log.info("Loc: "+loc.toString());
		}*/
	}
}
