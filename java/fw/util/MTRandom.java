/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fw.util;

import java.security.SecureRandom;

public class MTRandom
{
    private SecureRandom _random = new SecureRandom();
    
    private static MTRandom _instance;
    
    public SecureRandom getSecureRandom()
    {
    	return _random;
    }
    
    public static final MTRandom getInstance()
    {
        if (_instance == null)
            _instance = new MTRandom();
        return _instance;
    }
    
    private MTRandom()
    {
    }
}