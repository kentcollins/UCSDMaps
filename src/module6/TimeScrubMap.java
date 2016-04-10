package module6;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class TimeScrubMap extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	HashMap<String, List<Float>> data;
	List<Feature> countries;
	List<Marker> countryMarkers;
	int yearSelection = 0;
	int baseYear = 1996;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load serial values for each country
		data = ParseFeed.loadTimeSeriesFromCSV(this, "../data/Popular_Indicators_Data.csv", "IT.NET.USER.P2");

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "../data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		System.out.println(countryMarkers.get(0).getId());

		shadeCountries(0);
	}

	public void draw() {
		background(225);
		// Draw map tiles and country markers
		map.draw();
		pushStyle();
		fill(0);
		textAlign(CENTER);
		textSize(28);
		text("" + (yearSelection + baseYear), mouseX, 30);
		textSize(16);
		text("Move the mouse right/left to scrub the year being displayed.", width / 2, height - 30);
		stroke(255);
		line(mouseX, 50, mouseX, height-50);
		popStyle();
	}

	public void mouseMoved() {
		yearSelection = (int) map(mouseX, 0, this.width, 0, 20);
		shadeCountries(yearSelection);
	}

	// Helper method to color each country based on series values.
	// Series selected by relative location of mouseX across PApplet
	// Red-orange indicates low (near 40)
	// Blue indicates high (near 100)
	private void shadeCountries(int year) {

		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			if (data.containsKey(countryId)) {
				Float val = data.get(countryId).get(year);
				if (val != null) { // don't change color if no data
					// Encode value as blueness
					int colorLevel = (int) map(val, 0f, 100f, 0, 255);
					marker.setColor(color(255 - colorLevel, 200, colorLevel));
				}
			} else {
				// data not available for this country
				marker.setColor(color(255, 200, 0));
			}
		}
	}
}
