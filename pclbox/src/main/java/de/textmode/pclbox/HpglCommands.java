package de.textmode.pclbox;

/*
 * Copyright 2017 Michael Knigge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;

/**
 * This class provides a static {@link HashMap} with mappings of a "unique command string"
 * to a textual description of a {@link Pcl5Command}.
 */
final class HpglCommands {

    private static final HashMap<String, String> MAPPINGS = new HashMap<>();

    static {
        MAPPINGS.put("AA", "Absolute Arc");
        MAPPINGS.put("AC", "Anchor Corner");
        MAPPINGS.put("AC", "Alternate Font Definition");
        MAPPINGS.put("AF", "Advance Full Page");
        MAPPINGS.put("AH", "Advance Full Page");
        MAPPINGS.put("AP", "Automatic Pen Operations");
        MAPPINGS.put("AR", "Arc Relative Three Point");
        MAPPINGS.put("AS", "Acceleration Select");
        MAPPINGS.put("BF", "Buffer Plot");
        MAPPINGS.put("BL", "Buffer Label");
        MAPPINGS.put("BP", "Begin Plot");
        MAPPINGS.put("BR", "Bezier Relative");
        MAPPINGS.put("BZ", "Bezier Absolute");
        MAPPINGS.put("CA", "Designate Alternate Character Set");
        MAPPINGS.put("CC", "Character Chord Angle");
        MAPPINGS.put("CF", "Character Fill Mode");
        MAPPINGS.put("CI", "Circle");
        MAPPINGS.put("CM", "Character Selection Mode");
        MAPPINGS.put("CO", "Comment");
        MAPPINGS.put("CP", "Character Plot");
        MAPPINGS.put("CS", "Designate Standard Character Set");
        MAPPINGS.put("CT", "Chord Tolerance");
        MAPPINGS.put("CV", "Curved Line Generator");
        MAPPINGS.put("DC", "Digitize Clear");
        MAPPINGS.put("DF", "Default Values");
        MAPPINGS.put("DI", "Absolute Direction");
        MAPPINGS.put("DL", "Download Character");
        MAPPINGS.put("DP", "Digitize Point");
        MAPPINGS.put("DR", "Relative Direction");
        MAPPINGS.put("DS", "Designate Character Into Slot");
        MAPPINGS.put("DT", "Define Label Terminator");
        MAPPINGS.put("DV", "Define Variable Text Path");
        MAPPINGS.put("EA", "Edge Rectangle Absolute");
        MAPPINGS.put("EC", "Enable Cutter");
        MAPPINGS.put("EP", "Edge Polygon");
        MAPPINGS.put("ER", "Edge Rectangle Relative");
        MAPPINGS.put("ES", "Extra Space");
        MAPPINGS.put("EW", "Edge Wedge");
        MAPPINGS.put("FI", "Primary Font Selection By ID");
        MAPPINGS.put("FN", "Secondary Font Selection By ID");
        MAPPINGS.put("FP", "Fill Polygon");
        MAPPINGS.put("FR", "Frame Advance");
        MAPPINGS.put("FS", "Force Select");
        MAPPINGS.put("FT", "Fill Type");
        MAPPINGS.put("GC", "Group Count");
        MAPPINGS.put("GM", "Graphics Memory");
        MAPPINGS.put("IM", "Input Mask");
        MAPPINGS.put("IN", "Initialize");
        MAPPINGS.put("IP", "Input P1 And P2");
        MAPPINGS.put("IR", "Input Relative P1 And P2");
        MAPPINGS.put("IV", "Invoke Character Slot");
        MAPPINGS.put("IW", "Input Window");
        MAPPINGS.put("KY", "Define Key");
        MAPPINGS.put("LA", "Line Attributes");
        MAPPINGS.put("LB", "Label");
        MAPPINGS.put("LM", "Label Mode");
        MAPPINGS.put("LO", "Label Origin");
        MAPPINGS.put("LT", "Line type");
        MAPPINGS.put("MC", "Merge Control");
        MAPPINGS.put("MG", "Message");
        MAPPINGS.put("MT", "Media Type");
        MAPPINGS.put("NP", "Number Of Pens");
        MAPPINGS.put("NR", "Not Ready");
        MAPPINGS.put("OA", "Output Actual position And Pen Status");
        MAPPINGS.put("OC", "Output Commanded Position And Pen Status");
        MAPPINGS.put("OD", "Output Digitized Point And Pen Status");
        MAPPINGS.put("OE", "Output Error");
        MAPPINGS.put("OF", "Output Factors");
        MAPPINGS.put("OG", "Output Group Count");
        MAPPINGS.put("OH", "Output Hard-Clip Limits");
        MAPPINGS.put("OI", "Output Identification");
        MAPPINGS.put("OK", "Output Key");
        MAPPINGS.put("OL", "Output Label Length");
        MAPPINGS.put("OO", "Output options");
        MAPPINGS.put("OP", "Output P1 And P2");
        MAPPINGS.put("OS", "Output Status");
        MAPPINGS.put("OT", "Output Carousel Type");
        MAPPINGS.put("OW", "Output Window");
        MAPPINGS.put("PA", "Plot Absolute");
        MAPPINGS.put("PB", "Print Buffered Label");
        MAPPINGS.put("PC", "Pen Color Assignment");
        MAPPINGS.put("PD", "Pen Down");
        MAPPINGS.put("PE", "Polyline Encoded");
        MAPPINGS.put("PG", "Advance Full Page");
        MAPPINGS.put("PM", "Polygon Mode");
        MAPPINGS.put("PP", "Pixel Placement");
        MAPPINGS.put("PR", "Plot Relative");
        MAPPINGS.put("PS", "Plot Size");
        MAPPINGS.put("PT", "Pen Thickness");
        MAPPINGS.put("PU", "Pen Up");
        MAPPINGS.put("RA", "Fill Rectangle Absolute");
        MAPPINGS.put("RO", "Rotate Coordinate System");
        MAPPINGS.put("RP", "Replot");
        MAPPINGS.put("RR", "Fill Rectangle Relative");
        MAPPINGS.put("RT", "Relative Arc Three Point");
        MAPPINGS.put("SA", "Select Alternate Font");
        MAPPINGS.put("SB", "Scaleable Or Bitmap Fonts");
        MAPPINGS.put("SC", "Scale");
        MAPPINGS.put("SD", "Standard Font Definition");
        MAPPINGS.put("SI", "Absolute Character Size");
        MAPPINGS.put("SL", "Character Slant");
        MAPPINGS.put("SM", "Symbol Mode");
        MAPPINGS.put("SP", "Select Pen");
        MAPPINGS.put("SR", "Relative Character Size");
        MAPPINGS.put("SS", "Select Standard Font");
        MAPPINGS.put("ST", "Sort");
        MAPPINGS.put("SC", "Screened Vectors");
        MAPPINGS.put("TD", "Transparent Data");
        MAPPINGS.put("TL", "Tick Length");
        MAPPINGS.put("TR", "Transparency Mode");
        MAPPINGS.put("UC", "User-Defined Character");
        MAPPINGS.put("UF", "User-Defined Fill Type");
        MAPPINGS.put("UL", "User-Defined Line Type");
        MAPPINGS.put("VS", "Velocity Select");
        MAPPINGS.put("WD", "Write To Display");
        MAPPINGS.put("WG", "Fill Wedge");
        MAPPINGS.put("WU", "Pen Width Unit Selection");
        MAPPINGS.put("XT", "X-Tick");
        MAPPINGS.put("YT", "Y-Tick");
    }

    /**
     * Returns a textual description of the {@link HpglCommand}.
     *
     * @param command   the {@link HpglCommand} for which a textual description shall be returned.
     *
     * @return a textual description the the {@link HpglCommand}.
     */
    static String getCommandDescriptionFor(final HpglCommand command) {
        final String cmdString = command.toCommandString();
        final String result = MAPPINGS.get(cmdString);

        return result == null ? "Unknown HP/GL-Command " + cmdString : result;
    }
}
