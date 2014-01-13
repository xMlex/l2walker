unit uMap;

interface

uses Generics.Collections, Types, GLHUDObjects, GLWindowsFont, GLCanvas,
  GLObjects, GLMaterial, GLScene, SysUtils, GLCoordinates, Windows,
  GLRenderContextInfo, Math, xbWorldObjects, xbRegions;

const
  cMapSize = 2048;
  mapBlockSize = 32768;
  mapBlockSizeDiv3 = mapBlockSize div 3;
  MAPBLOCKSIZEDIV900 = mapBlockSize div 900;
  MAPBLOCKSIZEfMAP = mapBlockSize div 2048;
  fileSize = cMapSize div 3;

  cWorldParameter = 0.0625;

type
  TL2OnMapRender = procedure(aCanvas: TGLCanvas) of object;

  TL2Object = class
  public
    Name: string;
    Location: TL2Loc;
    MapCachePos: TFloatPoint;
    constructor Create(aLoc: TL2Loc; aName: string);
  end;

  TL2Map = class
  private
    GLObject: TGLHUDSprite;
    currentBlock: TL2Loc;
    MaterialLibrary: TGLMaterialLibrary;
    mapStartLocation: TL2Loc;
    fCanvas: TGLCanvas;
    fRenderContextInfo: TRenderContextInfo;
    fBoxSize: Integer;
    fOnRender: TL2OnMapRender;
    fLoadMap: Boolean;
    function MapBlockByLoc(aLoc: TL2Loc): TL2Loc;
  public
    Me: TL2SpawnObject;
    Objects: TList<TL2Object>;
    Font: TGLWindowsBitmapFont;
    Scale: Single;
    procedure RndBox(x, y: Single);
    procedure RndText(aLoc: TFloatPoint; aText: String; aColor: Integer = 255);
    procedure RndElipse(aLoc: TFloatPoint);
    function L2LocToMapLoc(aLoc: TL2Loc): TFloatPoint;
    function L2LocToMapLoc2(aLoc: TL2Loc): TL2Loc;
    function MapLocToL2Loc(x, y: Integer): TL2Loc;
    procedure Draw(aCanvas: TGLCanvas; aRenderContextInfo: TRenderContextInfo);
    constructor Create(aGLObject: TGLHUDSprite; aFont: TGLWindowsBitmapFont;
      aMaterialLibrary: TGLMaterialLibrary);
    property OnMapRender: TL2OnMapRender read fOnRender write fOnRender;
    property BoxSize: Integer read fBoxSize write fBoxSize;
    property LoadMap: Boolean read fLoadMap write fLoadMap;
  end;

implementation

uses uGLFunctions, IOUtils, Graphics;

function TL2Map.L2LocToMapLoc(aLoc: TL2Loc): TFloatPoint;
begin
  Result.x := Me.MapCachePos.x + (aLoc.x - Me.Loc.x) * Scale * cWorldParameter;
  Result.y := Me.MapCachePos.y + (aLoc.y - Me.Loc.y) * Scale * cWorldParameter;
end;

procedure TL2Map.Draw(aCanvas: TGLCanvas;
  aRenderContextInfo: TRenderContextInfo);
var
  mapBlock: TL2Loc;
  materialName: string;
  i: Int32;
  o: TL2Object;
  temp: TFloatPoint;
begin
  fCanvas := aCanvas;
  fRenderContextInfo := aRenderContextInfo;
  fBoxSize := Round(Scale) + 3;
  mapBlock := MapBlockByLoc(Me.Loc);
  if (mapBlock <> currentBlock) and (fLoadMap) then
  begin
    currentBlock := mapBlock;
    materialName := Format('%d_%d', [currentBlock.x, currentBlock.y]);
    if TFile.Exists('maps\' + materialName + '.jpg') then
      GLObject.Material := AddMaterial(MaterialLibrary, 'maps\' + materialName +
        '.jpg', materialName).Material;
  end;

  Me.MapCachePos.x := aCanvas.CanvasSizeX / 2;
  Me.MapCachePos.y := aCanvas.CanvasSizeY / 2;

  GLObject.SetSize(mapBlockSize * Scale * cWorldParameter,
    mapBlockSize * Scale * cWorldParameter);
  temp := L2LocToMapLoc(mapStartLocation);
  GLObject.Position.x := temp.x + mapBlockSize * Scale * cWorldParameter / 2;
  GLObject.Position.y := temp.y + mapBlockSize * Scale * cWorldParameter / 2;

  if Assigned(fOnRender) then
    fOnRender(aCanvas);
end;

function TL2Map.L2LocToMapLoc2(aLoc: TL2Loc): TL2Loc;
begin
  Result.x := Round(Me.MapCachePos.x + (aLoc.x - Me.Loc.x) * Scale *
    cWorldParameter);
  Result.y := Round(Me.MapCachePos.y + (aLoc.y - Me.Loc.y) * Scale *
    cWorldParameter);
end;

function TL2Map.MapBlockByLoc(aLoc: TL2Loc): TL2Loc;
var
  bx, by: Integer;
begin
  mapStartLocation.x := floor(aLoc.x / mapBlockSize) * mapBlockSize;
  mapStartLocation.y := floor(aLoc.y / mapBlockSize) * mapBlockSize;

  aLoc.x := aLoc.x + 131035;
  aLoc.y := aLoc.y + 262053;
  Result.x := aLoc.x div mapBlockSize;
  Result.y := aLoc.y div mapBlockSize;
  Result.z := 0;

  Inc(Result.x, 16);
  Inc(Result.y, 10);
end;

function TL2Map.MapLocToL2Loc(x, y: Integer): TL2Loc;
begin
  Result.x := Round(Me.Loc.x + (x - Me.MapCachePos.x) / Scale /
    cWorldParameter);
  Result.y := Round(Me.Loc.y + (y - Me.MapCachePos.y) / Scale /
    cWorldParameter);
end;

procedure TL2Map.RndBox(x, y: Single);
var
  tar: array [0 .. 3] of TPoint;
begin
  // SetLength(tar,4);
  tar[0].x := Round(x - fBoxSize);
  tar[0].y := Round(y - fBoxSize);
  tar[1].x := Round(x + fBoxSize);
  tar[1].y := Round(y - fBoxSize);
  tar[2].x := Round(x + fBoxSize);
  tar[2].y := Round(y + fBoxSize);
  tar[3].x := Round(x - fBoxSize);
  tar[3].y := Round(y + fBoxSize);
  fCanvas.Polygon(tar);
end;

procedure TL2Map.RndElipse(aLoc: TFloatPoint);
begin
  fCanvas.Ellipse(aLoc.x, aLoc.y, fBoxSize, fBoxSize);
end;

procedure TL2Map.RndText(aLoc: TFloatPoint; aText: String;
  aColor: Integer = 255);
var
  tw, th: Integer;
begin
  tw := Font.TextWidth(aText);
  th := Font.CharHeight;
  Font.TextOut(fRenderContextInfo, aLoc.x - tw div 2, aLoc.y - fBoxSize - 2 -
    th, aText, aColor);
end;

constructor TL2Object.Create(aLoc: TL2Loc; aName: string);
begin
  inherited Create;
  Location := aLoc;
  Name := aName;
end;

constructor TL2Map.Create(aGLObject: TGLHUDSprite; aFont: TGLWindowsBitmapFont;
  aMaterialLibrary: TGLMaterialLibrary);
begin
  inherited Create;
  Me := nil;
  Objects := TList<TL2Object>.Create;
  GLObject := aGLObject;
  GLObject.SetSize(cMapSize, cMapSize);
  Scale := 1.0;
  currentBlock := TL2Loc.Create(0, 0, 0);
  Font := aFont;
  MaterialLibrary := aMaterialLibrary;
end;

end.
