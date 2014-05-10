package com.kindborg.mattias.doodle;

import java.util.*;

import com.kindborg.mattias.doodle.DoodleView.*;

import android.app.*;
import android.os.*;

public class MainActivity extends Activity {

    private static final String INSTANCESTATE_DOODLE = "instancestate_doodle";

    private DoodleView doodleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleView = (DoodleView) findViewById(R.id.mainview_doodleview);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(INSTANCESTATE_DOODLE, convertToString(doodleView.getPoints()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        doodleView.setPoints(convertToPoints(savedInstanceState.getString(INSTANCESTATE_DOODLE)));
    }

    // The following rules define the format points are saved in:
    //   * Points are separated using the delimiter ,
    //   * A point consists of two coordinates, these are separated by the character ;
    //   * A point will have the following format:
    //         X;Y            X and Y are coordinates of type float
    //   * A point acting as stop will have the following format:
    //         X;Y]           X and Y are coordinates of type float
    //                        ] marks the point as a stop
    private static String convertToString(List<Point> points) {
        StringBuilder pointsAsString = new StringBuilder();

        for (Point point : points) {
            // Append delimiter
            if (pointsAsString.length() > 0) {
                pointsAsString.append(",");
            }

            // Append coordinates
            pointsAsString.append(String.format("%s;%s", point.getX(), point.getY()));

            // Append if point is stop
            if (point.getIsStop()) {
                pointsAsString.append("]");
            }
        }

        return pointsAsString.toString();
    }

    private static List<Point> convertToPoints(String string) {
        List<Point> points = new ArrayList<Point>();

        // Special case for empty string
        if (string.isEmpty()) {
            return points;
        }

        for (String pointAsString : string.split(",")) {
            // Is point stop?
            boolean isStop = false;
            if (pointAsString.endsWith("]")) {
                isStop = true;
                pointAsString = pointAsString.replace("]", "");
            }

            // Coordinates
            String[] coordinates = pointAsString.split(";");
            float x = Float.parseFloat(coordinates[0]);
            float y = Float.parseFloat(coordinates[1]);

            Point point = isStop ? Point.createStop(x, y) : Point.create(x, y);
            points.add(point);
        }

        return points;
    }
}