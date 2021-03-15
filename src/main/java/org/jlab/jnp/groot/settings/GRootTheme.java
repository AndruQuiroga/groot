/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.settings;

import java.awt.BasicStroke;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.utils.json.*;

/**
 *
 * @author gavalian
 */
public class GRootTheme {
    
    public enum ThemeFontType {
        AXIS_TICKS_FONT, AXIS_TITLE_FONT, REGION_TITLE_FONT
    };
    
    public static GRootTheme GROOTTheme = new GRootTheme();
    public static GRootTheme getInstance(){ return GROOTTheme;}
    
    public static List<float[]> dashPatterns = Arrays.asList(
            new float[]{10.0f,0.0f},
            new float[]{10.0f,5.0f},
            new float[]{10.0f,5.0f,2.0f,5.0f},
            new float[]{2.0f,5.0f,2.0f,5.0f},
            new float[]{2.0f,8.0f,2.0f,4.0f},
            new float[]{2.0f,6.0f,2.0f,2.0f},
            new float[]{1.0f,10.0f},
            new float[]{1.0f,1.0f},
            new float[]{5.0f,10.0f},
            new float[]{5.0f,5.0f},
            new float[]{5.0f,1.0f},            
            new float[]{3.0f,10.0f,1.0f,10.0f},
            new float[]{3.0f,5.0f,1.0f,5.0f},
            new float[]{3.0f,10.0f,1.0f,10.0f,1.0f,10.0f},
            new float[]{3.0f,1.0f,1.0f,1.0f,1.0f,1.0f}                                    
    );
    
    private  GRootColorPalette  themePalette = new GRootColorPalette();        
        
    private AttributeCollection dataSetAttributes = null;
    private AttributeCollection    axisAttributes = null;
    private AttributeCollection  regionAttributes = null;

    
    private Map<ThemeFontType, Font> themeFonts = new HashMap<>();
    
    public GRootTheme(){
        initAttributes();
        initFonts();
        themePalette.setColorScheme("tab10");
    }
    
    public GRootColorPalette getPalette(){ return themePalette;}
    public AttributeCollection getAxisAttributes(){ return axisAttributes;}
    public AttributeCollection getRegionAttributes(){ return regionAttributes;}
    
    protected final void initAttributes(){

        axisAttributes = new AttributeCollection(
                new AttributeType[]{
                        AttributeType.AXISLINECOLOR,
                        AttributeType.AXISLINEWIDTH, AttributeType.AXISLINESTYLE,
                        AttributeType.AXISTICKSIZE,AttributeType.AXISLABELOFFSET,
                        AttributeType.AXISTITLEOFFSET,AttributeType.AXISTITLEOFFSETVERTICAL,
                        AttributeType.AXISDRAWBOX,AttributeType.AXISDRAWTICKS,
                        AttributeType.AXISDRAWLINE,AttributeType.AXISDRAWGRID,
                        AttributeType.AXIS_DRAW_LABELS,AttributeType.AXIS_DRAW_TITLE,
                        AttributeType.AXIS_DRAW_TICKS
                },
                new String[]{"0","1","1","5","5","10","10",
                    "true","true","true","false","true","true","true"});

        regionAttributes = new AttributeCollection(
                new AttributeType[]{
                        AttributeType.FILLCOLOR, AttributeType.PAD_MARGIN_TOP,
                        AttributeType.PAD_MARGIN_LEFT, AttributeType.PAD_MARGIN_RIGHT,
                        AttributeType.PAD_MARGIN_BOTTOM
                },
                new String[]{"0", "10", "40", "40", "10"});
        
    }
    
    
    public final void initFonts(){
        themeFonts.put(ThemeFontType.AXIS_TICKS_FONT, new Font("Avenir",Font.PLAIN,14));
        themeFonts.put(ThemeFontType.AXIS_TITLE_FONT, new Font("Avenir",Font.PLAIN,18));
        themeFonts.put(ThemeFontType.REGION_TITLE_FONT, new Font("Avenir",Font.PLAIN,18));
    }
    
    public final void initFont(String theme){
        if(theme.compareTo("PAW")==0){
            themeFonts.put(ThemeFontType.AXIS_TICKS_FONT, new Font("Times",Font.PLAIN,14));
            themeFonts.put(ThemeFontType.AXIS_TITLE_FONT, new Font("Times",Font.PLAIN,18));
            themeFonts.put(ThemeFontType.REGION_TITLE_FONT, new Font("Times",Font.PLAIN,18));
        }
    }
    public Font getFont(ThemeFontType type){
        return themeFonts.get(type);
    }
    
    public void setFont(ThemeFontType type, Font font){
        themeFonts.put(type, font);
    }
    
    public BasicStroke getLineStroke(int style, int width){
        
        if(style<2) return new BasicStroke(width);
        int strokeStyle = style;
        if(style>=dashPatterns.size())
            strokeStyle= style%dashPatterns.size();
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPatterns.get(strokeStyle), 0.0f);

            /*
         switch (style){                
                case 1 : return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f);
                case 2: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern2, 0.0f);
                case 3: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern3, 0.0f);
                case 4: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern4, 0.0f);
                case 5: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern5, 0.0f);
                default : return new BasicStroke(width);

            }*/
    }

    public void applyTheme(int ThemeID) {
        JsonObject jsonParser = null;

        try {
            String fileName = getClass().getClassLoader().getResource("json/themes.json").getFile();
            FileReader reader = new FileReader(fileName);
            jsonParser = (JsonObject) Json.parse(reader);
        } catch (NullPointerException | FileNotFoundException e){
            System.err.println("FileNotFoundError:\nRESOURCE: 'themes' NOT FOUND!");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JsonArray themes = (JsonArray) jsonParser.get("themes");
        JsonObject targetTheme = (JsonObject) themes.get(ThemeID);

        JsonArray jsonAxisAttributes = (JsonArray) targetTheme.get("axisAttributes");
        extractAttributes(jsonAxisAttributes, axisAttributes);

        JsonArray jsonRegionAttributes = (JsonArray) targetTheme.get("regionAttributes");
        extractAttributes(jsonRegionAttributes, regionAttributes);

    }

    public static String[] getThemeNames(){
        JsonObject jsonParser = null;

        try {
            String fileName = GRootTheme.class.getClassLoader().getResource("json/themes.json").getFile();
            FileReader reader = new FileReader(fileName);
            jsonParser = (JsonObject) Json.parse(reader);
        } catch (NullPointerException | FileNotFoundException e){
            System.err.println("FileNotFoundError:\nRESOURCE: 'themes' NOT FOUND!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        JsonArray themes = (JsonArray) jsonParser.get("themes");
        String[] themeNames = new String[themes.size()];

        for (int i = 0; i < themes.size(); i++) {
            JsonObject jTheme = (JsonObject) themes.get(i);
            themeNames[i] = jTheme.get("themeName").asString();
        }

        return themeNames;
    }

    private static void extractAttributes(JsonArray jsonAttributes, AttributeCollection Attributes) {
        Arrays.stream(AttributeType.values()).forEach(Attribute -> {
            Optional<JsonValue> targetJsonAttribute = jsonAttributes.values().stream().filter(jsonAttribute ->
                    jsonAttribute.asObject().get("id").asInt() == Attribute.getId()).findFirst();

            targetJsonAttribute.ifPresent(jsonAttribute ->
                    Attributes.changeValue(Attribute,  String.valueOf(jsonAttribute.asObject().get("value"))));
        });
    }
}
