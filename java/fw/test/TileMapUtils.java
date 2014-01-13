package fw.test;

/**
 * --Terms used (cause I keep forgetting--
 * clickX: the raw vertical coord of the click, based on the resolution of the game screen
 * clickY: the raw horizontal coord of the click, based on the resolution of the game screen
 * tileX: the vertical coord of of the tile, based on the tile map
 * tileY: the horizontal coord of of the tile, based on the tile map
 * mapX: the vertical coord of the tile, interpolated from the clickY
 * mapY: the horizontal coord of the tile, interpolated from the clickX
 */

import org.newdawn.slick.tiled.TiledMap;

public class TileMapUtils
{
    
    public static int getMapXFromClickX(int clickX, TiledMap map)
    {
        return TileMapUtils.getMapXFromTileX(
                                TileMapUtils.getTileXfromMapX(clickX, map), 
                                map);
    }
    
    public static int getMapYFromClickY(int clickY, TiledMap map)
    {
        return TileMapUtils.getMapYFromTileY(
                                TileMapUtils.getTileYfromMapY(clickY, map), 
                                map);
    }

    /**
     * Find the map-relational vertical coordinate from a resolution-relational tile vertical coordinate
     * @param tileX
     * @param map
     * @return
     */
    public static int getMapXFromTileX(int tileX, TiledMap map)
    {
        return tileX * map.getTileHeight();
    }

    /**
     * Find the map-relational horizontal coordinate from a resolution-relational tile horizontal coordinate
     * @param tileY
     * @param map
     * @return
     */
    public static int getMapYFromTileY(int tileY, TiledMap map)
    {
        return tileY * map.getTileWidth();
    }

    /**
     * Find the tile vertical coordinate from a click vertical coordinate
     * Relational to the tilemap
     * @param mapX
     * @param map
     * @return
     */
    public static int getTileXfromMapX(int mapX, TiledMap map)
    {
        return Math.round(mapX/map.getTileHeight());
    }

    /**
     * Find the tile horizontal coordinate from a click horizontal coordinate
     * Relational to the tilemap
     * @param mapY
     * @param map
     * @return
     */
    public static int getTileYfromMapY(int mapY, TiledMap map)
    {
        return Math.round(mapY/map.getTileWidth());
    }
}