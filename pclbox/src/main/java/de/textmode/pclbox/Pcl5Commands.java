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
final class Pcl5Commands {

    private static final HashMap<String, String> MAPPINGS = new HashMap<>();

    static {
        MAPPINGS.put("E", "Printer Reset");
        MAPPINGS.put("9", "Clear Horizontal Margins");
        MAPPINGS.put("=", "Half Line Feed");
        MAPPINGS.put("Y", "Display Functions Enable");
        MAPPINGS.put("Z", "Display Functions Disable");
        MAPPINGS.put("%A", "Enter PCL Mode");
        MAPPINGS.put("%B", "Enter HP-GL/2 Mode");
        MAPPINGS.put("%X", "Universal Exit Language");
        MAPPINGS.put("&aC", "Horizontal Cursor Positioning (Column)");
        MAPPINGS.put("&aG", "Duplex Page Side Selection");
        MAPPINGS.put("&aH", "Horizontal Cursor Positioning (Decipoints)");
        MAPPINGS.put("&aL", "Left Margin");
        MAPPINGS.put("&aM", "Right Margin");
        MAPPINGS.put("&aN", "Negative Motion");
        MAPPINGS.put("&aP", "Print Direction");
        MAPPINGS.put("&aR", "Vertical Cursor Positioning (Rows)");
        MAPPINGS.put("&aT", "Set Horizontal Tab");
        MAPPINGS.put("&aU", "Clear Horizontal Tab");
        MAPPINGS.put("&aV", "Vertical Cursor Positioning (Decipoints)");
        MAPPINGS.put("&aW", "User Defined Logical Page");
        MAPPINGS.put("&bF", "Finish Mode");
        MAPPINGS.put("&bM", "Monochrome Print Mode");
        MAPPINGS.put("&bT", "Dry Time");
        MAPPINGS.put("&bW", "Configuration (AppleTalk)");
        MAPPINGS.put("&cT", "Character Text Path Direction");
        MAPPINGS.put("&cW", "Cluster Printing");
        MAPPINGS.put("&d@", "Disable Underline");
        MAPPINGS.put("&dD", "Enable Underline");
        MAPPINGS.put("&fF", "Media Eject Length (Decipoints)");
        MAPPINGS.put("&fG", "Page Width (Decipoints)");
        MAPPINGS.put("&fS", "Push / Pop Cursor Position");
        MAPPINGS.put("&fX", "Macro Control");
        MAPPINGS.put("&fY", "Macro ID");
        MAPPINGS.put("&iW", "Underware Function Configuration");
        MAPPINGS.put("&kE", "Underline Enhancement");
        MAPPINGS.put("&kF", "Shift In/Out Control");
        MAPPINGS.put("&kG", "Line Termination");
        MAPPINGS.put("&kH", "Horizontal Motion Index (1/120 inches)");
        MAPPINGS.put("&kI", "Character Set Selection Control");
        MAPPINGS.put("&kS", "Pitch Mode");
        MAPPINGS.put("&kV", "Head View Mode");
        MAPPINGS.put("&kW", "Set Print Mode");
        MAPPINGS.put("&lA", "Page Size");
        MAPPINGS.put("&lC", "Vertical Motion Index");
        MAPPINGS.put("&lD", "Line Spacing");
        MAPPINGS.put("&lE", "Top Margin");
        MAPPINGS.put("&lF", "Text Length");
        MAPPINGS.put("&lG", "Output Bin Selection");
        MAPPINGS.put("&lH", "Paper Source");
        MAPPINGS.put("&lJ", "Auto Justification");
        MAPPINGS.put("&lL", "Perforation Skip");
        MAPPINGS.put("&lM", "Media Type");
        MAPPINGS.put("&lO", "Page Orientation");
        MAPPINGS.put("&lP", "Page Length (Lines)");
        MAPPINGS.put("&lR", "Clear Vertical Tab Absolute (Line)");
        MAPPINGS.put("&lS", "Simplex/Duplex Print");
        MAPPINGS.put("&lT", "Job Separation");
        MAPPINGS.put("&lU", "Left Offset Registration (Decipoints)");
        MAPPINGS.put("&lV", "Vertical Position Via VFC (Channel)");
        MAPPINGS.put("&lW", "Define VFC Table");
        MAPPINGS.put("&lX", "Number Of Copies");
        MAPPINGS.put("&lY", "Set Vertical Tab Absolute (Line)");
        MAPPINGS.put("&lZ", "Top Offset Registration (Decipoints)");
        MAPPINGS.put("&nW", "Paper Type");
        MAPPINGS.put("&pC", "Palette Control");
        MAPPINGS.put("&pI", "Palette Control ID");
        MAPPINGS.put("&pS", "Select Palette");
        MAPPINGS.put("&pW", "Escapement Encapsulated Text");
        MAPPINGS.put("&pX", "Transparent Print Data");
        MAPPINGS.put("&rF", "Flush All Pages");
        MAPPINGS.put("&sC", "End Of Line Wrap");
        MAPPINGS.put("&sI", "Character Set Default Control");
        MAPPINGS.put("&tP", "Text Parsing Method");
        MAPPINGS.put("&uD", "Unit Of Measure");
        MAPPINGS.put("&vS", "Text Color");
        MAPPINGS.put("(@", "Select Primary Default Font");
        MAPPINGS.put("(A", "Primary Symbol Set");
        MAPPINGS.put("(B", "Primary Symbol Set");
        MAPPINGS.put("(C", "Primary Symbol Set");
        MAPPINGS.put("(D", "Primary Symbol Set");
        MAPPINGS.put("(E", "Primary Symbol Set");
        MAPPINGS.put("(F", "Primary Symbol Set");
        MAPPINGS.put("(G", "Primary Symbol Set");
        MAPPINGS.put("(H", "Primary Symbol Set");
        MAPPINGS.put("(I", "Primary Symbol Set");
        MAPPINGS.put("(J", "Primary Symbol Set");
        MAPPINGS.put("(K", "Primary Symbol Set");
        MAPPINGS.put("(L", "Primary Symbol Set");
        MAPPINGS.put("(M", "Primary Symbol Set");
        MAPPINGS.put("(N", "Primary Symbol Set");
        MAPPINGS.put("(O", "Primary Symbol Set");
        MAPPINGS.put("(P", "Primary Symbol Set");
        MAPPINGS.put("(Q", "Primary Symbol Set");
        MAPPINGS.put("(R", "Primary Symbol Set");
        MAPPINGS.put("(S", "Primary Symbol Set");
        MAPPINGS.put("(T", "Primary Symbol Set");
        MAPPINGS.put("(U", "Primary Symbol Set");
        MAPPINGS.put("(V", "Primary Symbol Set");
        MAPPINGS.put("(W", "Primary Symbol Set");
        MAPPINGS.put("(X", "Primary Font Selection By ID");
        MAPPINGS.put("(Y", "Primary Symbol Set");
        MAPPINGS.put("(Z", "Primary Symbol Set");
        MAPPINGS.put("(fW", "Define Symbol Set");
        MAPPINGS.put("(sB", "Primary Stroke Weight");
        MAPPINGS.put("(sH", "Primary Pitch (Characters Per Inch)");
        MAPPINGS.put("(sP", "Primary Spacing");
        MAPPINGS.put("(sQ", "Primary Quality");
        MAPPINGS.put("(sS", "Primary Style");
        MAPPINGS.put("(sT", "Primary Typeface");
        MAPPINGS.put("(sU", "Primary Placement");
        MAPPINGS.put("(sV", "Primary Height (Points)");
        MAPPINGS.put("(sW", "Character Definition");
        MAPPINGS.put(")@", "Select Secondary Default Font");
        MAPPINGS.put(")A", "Secondary Symbol Set");
        MAPPINGS.put(")B", "Secondary Symbol Set");
        MAPPINGS.put(")C", "Secondary Symbol Set");
        MAPPINGS.put(")D", "Secondary Symbol Set");
        MAPPINGS.put(")E", "Secondary Symbol Set");
        MAPPINGS.put(")F", "Secondary Symbol Set");
        MAPPINGS.put(")G", "Secondary Symbol Set");
        MAPPINGS.put(")H", "Secondary Symbol Set");
        MAPPINGS.put(")I", "Secondary Symbol Set");
        MAPPINGS.put(")J", "Secondary Symbol Set");
        MAPPINGS.put(")K", "Secondary Symbol Set");
        MAPPINGS.put(")L", "Secondary Symbol Set");
        MAPPINGS.put(")M", "Secondary Symbol Set");
        MAPPINGS.put(")N", "Secondary Symbol Set");
        MAPPINGS.put(")O", "Secondary Symbol Set");
        MAPPINGS.put(")P", "Secondary Symbol Set");
        MAPPINGS.put(")Q", "Secondary Symbol Set");
        MAPPINGS.put(")R", "Secondary Symbol Set");
        MAPPINGS.put(")S", "Secondary Symbol Set");
        MAPPINGS.put(")T", "Secondary Symbol Set");
        MAPPINGS.put(")U", "Secondary Symbol Set");
        MAPPINGS.put(")V", "Secondary Symbol Set");
        MAPPINGS.put(")W", "Secondary Symbol Set");
        MAPPINGS.put(")X", "Secondary Font Selection By ID");
        MAPPINGS.put(")Y", "Secondary Symbol Set");
        MAPPINGS.put(")Z", "Secondary Symbol Set");
        MAPPINGS.put(")sB", "Secondary Stroke Weight");
        MAPPINGS.put(")sH", "Secondary Font: Pitch (Characters Per Pnch)");
        MAPPINGS.put(")sP", "Secondary Spacing");
        MAPPINGS.put(")sQ", "Secondary Quality");
        MAPPINGS.put(")sS", "Secondary Style");
        MAPPINGS.put(")sT", "Secondary Typeface");
        MAPPINGS.put(")sU", "Secondary Placement");
        MAPPINGS.put(")sV", "Secondary Height (Points)");
        MAPPINGS.put(")sW", "Font Header");
        MAPPINGS.put("*bB", "Set Black Optimization");
        MAPPINGS.put("*bM", "Set Compression Method");
        MAPPINGS.put("*bS", "Seed Row Source (Plane)");
        MAPPINGS.put("*bV", "Transfer Raster Data By Plane");
        MAPPINGS.put("*bW", "Transfer Raster Data By Block");
        MAPPINGS.put("*bX", "Raster Line X Offset (Pixels)");
        MAPPINGS.put("*bY", "Raster Y Offset (Raster lines)");
        MAPPINGS.put("*cA", "Horizontal Rectangle Size By PCL-Units");
        MAPPINGS.put("*cB", "Vertical Rectangle Size By PCL-Units");
        MAPPINGS.put("*cC", "Large Character Placement (Column)");
        MAPPINGS.put("*cD", "Font ID");
        MAPPINGS.put("*cE", "Character Code");
        MAPPINGS.put("*cF", "Font Control");
        MAPPINGS.put("*cG", "Pattern ID");
        MAPPINGS.put("*cH", "Horizontal Rectangle Size By Decipoints");
        MAPPINGS.put("*cK", "Horizontal HP-GL/2 Plot Size (Inches)");
        MAPPINGS.put("*cL", "Vertical HP-GL/2 Plot Size (Inches)");
        MAPPINGS.put("*cM", "Large Character Size (Magnification)");
        MAPPINGS.put("*cN", "Large Character Tab");
        MAPPINGS.put("*cP", "Fill Rectangular Area");
        MAPPINGS.put("*cQ", "Pattern Control");
        MAPPINGS.put("*cR", "Symbol Set ID Code");
        MAPPINGS.put("*cS", "Symbol Set Control");
        MAPPINGS.put("*cT", "Set Picture Frame Anchor Point");
        MAPPINGS.put("*cV", "Vertical Rectangle Size By Decipoints");
        MAPPINGS.put("*cW", "User Defined Pattern");
        MAPPINGS.put("*cX", "Horizontal Picture Frame Size (Decipoints)");
        MAPPINGS.put("*cY", "Vertical Picture Frame Size (Decipoints)");
        MAPPINGS.put("*cZ", "Large Character Print Data");
        MAPPINGS.put("*gW", "Configure Raster Data");
        MAPPINGS.put("*iW", "Viewing Illuminant");
        MAPPINGS.put("*lO", "Logical Operation (ROP3)");
        MAPPINGS.put("*lP", "Clip Mask");
        MAPPINGS.put("*lR", "Pixel Placement");
        MAPPINGS.put("*lW", "Color Lookup Tables");
        MAPPINGS.put("*mW", "Download Dither Matrix");
        MAPPINGS.put("*oD", "Color Raster Depletion");
        MAPPINGS.put("*oM", "Print Quality");
        MAPPINGS.put("*oQ", "Mechanical Print Quality");
        MAPPINGS.put("*oW", "Driver Configuration");
        MAPPINGS.put("*pN", "Set Graphics Print Mode");
        MAPPINGS.put("*pP", "Push / Pop Palette");
        MAPPINGS.put("*pR", "Set Pattern Reference Point");
        MAPPINGS.put("*pX", "Horizontal Cursor Positioning (PCL-Units)");
        MAPPINGS.put("*pY", "Vertical Cursor Positioning (PCL-Units)");
        MAPPINGS.put("*rA", "Start Raster Graphics");
        MAPPINGS.put("*rB", "End Raster Graphics (PCL4)");
        MAPPINGS.put("*rC", "End Raster Graphics");
        MAPPINGS.put("*rF", "Raster Graphics Presentation");
        MAPPINGS.put("*rL", "Horizontal Raster Resolution (Dots Per Inch)");
        MAPPINGS.put("*rQ", "Raster Graphics Quality");
        MAPPINGS.put("*rS", "Source Raster Width");
        MAPPINGS.put("*rT", "Source Raster Height");
        MAPPINGS.put("*rU", "Simple Color");
        MAPPINGS.put("*rV", "Vertical Raster Resolution (Dots Per Inch)");
        MAPPINGS.put("*sI", "Inquire Status Readback Entity");
        MAPPINGS.put("*sM", "Free Space");
        MAPPINGS.put("*sT", "Set Status Readback Location Type");
        MAPPINGS.put("*sU", "Set Status Readback Location Unit");
        MAPPINGS.put("*sX", "Echo");
        MAPPINGS.put("*s^", "Return Model Number");
        MAPPINGS.put("*tF", "QMS Magnum-5 Interpreter");
        MAPPINGS.put("*tG", "GPIS Data Binding");
        MAPPINGS.put("*tH", "Destination Raster Width (Decipoints)");
        MAPPINGS.put("*tI", "Gamma Correction");
        MAPPINGS.put("*tJ", "Render Algorithm");
        MAPPINGS.put("*tK", "Scale Algorithm");
        MAPPINGS.put("*tM", "Vector Graphics Operating Mode");
        MAPPINGS.put("*tN", "Vector Graphics Mapping Mode");
        MAPPINGS.put("*tP", "Vector Graphics Print Control");
        MAPPINGS.put("*tR", "Raster Graphics Resolution (Dots Per Inch)");
        MAPPINGS.put("*tV", "Destination Raster Height");
        MAPPINGS.put("*tW", "GPIS Data Transfer");
        MAPPINGS.put("*vA", "Color Component 1");
        MAPPINGS.put("*vB", "Color Component 2");
        MAPPINGS.put("*vC", "Color Component 3");
        MAPPINGS.put("*vI", "Assign Color Index");
        MAPPINGS.put("*vN", "Source Transparency Mode");
        MAPPINGS.put("*vO", "Pattern Transparency Mode");
        MAPPINGS.put("*vS", "Foreground Color");
        MAPPINGS.put("*vT", "Select Current Pattern");
        MAPPINGS.put("*vW", "Configure Image Data");
        MAPPINGS.put("*zC", "Bar Code Label Placement (Column)");
        MAPPINGS.put("*zH", "Bar Code Label Height (1/10 inches)");
        MAPPINGS.put("*zQ", "Bar Code Header Control");
        MAPPINGS.put("*zR", "Bar Code Wide Bar Width (Dots)");
        MAPPINGS.put("*zS", "Bar Code Narrow Bar Width (Dots)");
        MAPPINGS.put("*zT", "Bar Code Wide Space Width (Dots)");
        MAPPINGS.put("*zU", "Bar Code Narrow Space Width (Dots)");
        MAPPINGS.put("*zV", "Bar Code Selection");
        MAPPINGS.put("*zX", "Bar Code Label X Offset (Dots)");
        MAPPINGS.put("*zZ", "Bar Code Label");
    }

    /**
     * Returns a textual description of the {@link Pcl5Command}.
     *
     * @param command   the {@link Pcl5Command} for which a textual description shall be returned.
     *
     * @return a textual description the the {@link Pcl5Command}.
     */
    static String getCommandDescriptionFor(final Pcl5Command command) {
        final String cmdString = command.toCommandString();
        final String result = MAPPINGS.get(cmdString);

        return result == null ? "Unknown PCL-Command " + cmdString : result;
    }
}
