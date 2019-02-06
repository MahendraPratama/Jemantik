package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.graphics.Color;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mahendra on 09-Dec-18.
 */

public class CustomPolygon {
//    public static final int COLOR_BLACK_ARGB = Color.parseColor("#000");
//    public static final int COLOR_WHITE_ARGB = Color.parseColor("#fff");
//    public static final int COLOR_DARK_GREEN_ARGB = Color.parseColor("#A3062E02");
//    public static final int COLOR_GREEN_ARGB = Color.parseColor("#BC065302");
//    public static final int COLOR_YELLOW_ARGB = Color.parseColor("#A4FCEF00");
//    public static final int COLOR_ORANGE_ARGB = Color.parseColor("#ABDC850A");
//    public static final int COLOR_RED_ARGB = Color.parseColor("#A3FF1500");

    public static final int COLOR_PURPLE_ARGB = 0xff81C784;

    public static final int COLOR_BLUE_ARGB = 0xffF9A825;

    public static final int POLYGON_STROKE_WIDTH_PX = 4;
    public static final int PATTERN_DASH_LENGTH_PX = 10;
    public static final int PATTERN_GAP_LENGTH_PX = 5;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    public static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    public static void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor;
        int fillColor;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "Sangat Aman":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#A3062E02");
                fillColor = Color.parseColor("#A3062E02");
                break;
            case "Aman":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#BC065302");
                fillColor = Color.parseColor("#BC065302");
                break;
            case "Waspada":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#A4FCEF00");
                fillColor = Color.parseColor("#A4FCEF00");
                break;
            case "Bahaya":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#ABDC850A");
                fillColor = Color.parseColor("#ABDC850A");
                break;
            case "Sangat Bahaya":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#A3FF1500");
                fillColor = Color.parseColor("#A3FF1500");
                break;
            default:
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = Color.parseColor("#DE636262");
                fillColor = Color.parseColor("#9E565656");
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
        polygon.setClickable(true);
    }
}
