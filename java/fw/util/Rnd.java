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

/**$
 *
 * @author Balancer
 *
 */
public class Rnd
{
    public static final double get()	// get random number from 0 to 1
	{
        return MTRandom.getInstance().getSecureRandom().nextDouble();
	}

	/**
	 * Gets a random number from 0(inclusive) to n(exclusive)
	 *
	 * @param n The superior limit (exclusive)
	 * @return A number from 0 to n-1
	 */
    public static final int get(int n) // get random number from 0 to n-1
    {
        return (int) (MTRandom.getInstance().getSecureRandom().nextDouble() * n);
    }
    
    public static final int get(int min, int max) // get random number from min to max (not max-1 !)
    {
        return min + (int) Math.floor(MTRandom.getInstance().getSecureRandom().nextDouble() * (max - min + 1));
    }
    
    public static final int nextInt(int n)
    {
        return (int) Math.floor(MTRandom.getInstance().getSecureRandom().nextDouble() * n);
    }

    public static final int nextInt()
    {
        return MTRandom.getInstance().getSecureRandom().nextInt();
    }
    
    public static final double nextDouble()
    {
        return MTRandom.getInstance().getSecureRandom().nextDouble();
    }
    
    public static final double nextGaussian()
    {
        return MTRandom.getInstance().getSecureRandom().nextGaussian();
    }
    
    public static final boolean nextBoolean()
    {
        return MTRandom.getInstance().getSecureRandom().nextBoolean();
    }
    
    public static final void nextBytes(byte[] array)
    {
        MTRandom.getInstance().getSecureRandom().nextBytes(array);
    }
}
