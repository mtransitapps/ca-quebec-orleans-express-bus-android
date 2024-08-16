package org.mtransit.parser.ca_quebec_orleans_express_bus;

import static org.mtransit.commons.StringUtils.EMPTY;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.mt.data.MAgency;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// https://gtfs.keolis.ca/gtfs.zip
public class QuebecOrleansExpressBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new QuebecOrleansExpressBusAgencyTools().start(args);
	}

	@Nullable
	@Override
	public List<Locale> getSupportedLanguages() {
		return LANG_FR;
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Orleans Express";
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return false; // no route short name provided
	}

	@Override
	public boolean defaultRouteLongNameEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String cleanRouteLongName(@NotNull String routeLongName) {
		routeLongName = CleanUtils.SAINT.matcher(routeLongName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS1.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS1_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS2.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS2_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
	}

	@NotNull
	@Override
	public String provideMissingRouteShortName(@NotNull GRoute gRoute) {
		//noinspection deprecation
		switch (gRoute.getRouteId()) {
		case "1": // Montréal - Quebec (Express)
			return "MT QC S";
		case "2": // Service aéroport Trudeau // Montréal - Aéroport Montréal-Trudeau
			return "MT YUL";
		case "3": // Bas-Saint-Laurent
			return "QC RK";
		case "4": // Gaspésie (Sud)
			return "RK GS S";
		case "5": // Gaspésie (Nord)
			return "RK GS N";
		case "6": // Montréal - Québec (Mauricie)
			return "MT QC N";
		case "7": // Centre-du-Québec // Drummondville - Victoriaville
			return "MT VT";
		case "53": // Montréal - Ottawa
			return "MT OT";
		}
		return super.provideMissingRouteShortName(gRoute);
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	private static final String AGENCY_COLOR = "01ADB9";

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Nullable
	@Override
	public String provideMissingRouteColor(@NotNull GRoute gRoute) {
		// https://www.orleansexpress.com/fr/carte-du-reseau/
		//noinspection deprecation
		switch (gRoute.getRouteId()) {
		case "1": // Montréal - Quebec (Express)
			return "1E51A4";
		case "2": // Service aéroport Trudeau
			return "4E76BA";
		case "3": // Bas-Saint-Laurent
			return "F04E5E";
		case "4": // Gaspésie (Sud)
			return "3BA9BF";
		case "5": // Gaspésie (Nord)
			return "69BD45";
		case "6": // Montréal - Québec (Mauricie)
			return "F89843";
		case "7": // Centre-du-Québec // Drummondville - Victoriaville
			return "DB32Bf";
		case "53": // Montréal - Ottawa
			return "8224E3";
		}
		return super.provideMissingRouteColor(gRoute);
	}

	@Override
	public boolean directionSplitterEnabled(long routeId) {
		return true;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	private static final Pattern FIX_CENTRE_VILLE_ = Pattern.compile("((^|\\W)(centre ville)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String FIX_CENTRE_VILLE_REPLACEMENT = "$2" + "Centre-Ville" + "$4";

	private static final Pattern REMOVE_ENDS_W_CENTRE_VILLE_ = Pattern.compile("(( \\(centre-ville\\))(\\W|$))");

	private static final Pattern QUEBEC_UNIVERSITE_LAVAL_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(université laval\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
	private static final String QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT = "$2" + "Université Laval" + "$4";

	private static final Pattern MONTREAL_AEROPORT_TRUDEAU_ = Pattern.compile("((^|\\W)(montr[é|e]al \\(a[é|e]roport trudeau\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final String MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT = "$2" + "Aéroport Trudeau" + "$4";

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = FIX_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(FIX_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = REMOVE_ENDS_W_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = QUEBEC_UNIVERSITE_LAVAL_.matcher(tripHeadsign).replaceAll(QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT);
		tripHeadsign = MONTREAL_AEROPORT_TRUDEAU_.matcher(tripHeadsign).replaceAll(MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanBounds(Locale.FRENCH, tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypesFRCA(tripHeadsign);
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	@Override
	public @NotNull String cleanStopHeadSign(@NotNull String stopHeadsign) {
		return EMPTY; // stop head-signs are stop names
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.cleanBounds(Locale.FRENCH, gStopName);
		gStopName = CleanUtils.cleanStreetTypesFRCA(gStopName);
		return CleanUtils.cleanLabelFR(gStopName);
	}
}
