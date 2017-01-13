package mk.pclbox;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * JUnit-Tests for {@link PrinterCommandVisitor}.
 */
public final class PrinterCommandVisitorTest extends DataStreamParserTest {

    private static final CountingVisitor VISITOR = new CountingVisitor();

    @Override
    public void handlePrinterCommand(PrinterCommand command) {
        command.accept(VISITOR);
    }

    /**
     * Simple visitor that just counts the invocations.
     */
    private static class CountingVisitor implements PrinterCommandVisitor {

        private int textCommandCounter = 0;
        private int controlCharacterCounter = 0;
        private int twoBytePclCommandCounter = 0;
        private int parameterizedPclCommand = 0;
        private int pjlCommand = 0;
        private int hpglCommand = 0;

        @Override
        public void handle(TextCommand command) {
            ++this.textCommandCounter;
        }

        @Override
        public void handle(ControlCharacterCommand command) {
            ++this.controlCharacterCounter;
        }

        @Override
        public void handle(TwoBytePclCommand twoBytePclCommand) {
            ++this.twoBytePclCommandCounter;
        }

        @Override
        public void handle(ParameterizedPclCommand parameterizedPclCommand) {
            ++this.parameterizedPclCommand;
        }

        @Override
        public void handle(PjlCommand pjlCommand) {
            ++this.pjlCommand;
        }

        @Override
        public void handle(HpglCommand hpglCommand) {
            ++this.hpglCommand;
        }
    }

    /**
     * Parses a PCL data stream that contains just one concrete {@link PclCommand} of every type. Checks if
     * the visitor is invoked for every type once.
     */
    public void testVisitor() throws Exception {

        final byte[] twoByteCommandContent = { 0x1B, 'E' };
        final byte[] textCommandContent = { '1', '2', '3' };
        final byte[] controlCharacterContent = { 0x0C };
        final byte[] parameterizedPclCommandContent = { 0x1B, '%', '-', '1', '2', '3', '4', '5', 'X' };
        final byte[] enterHpglMode = { 0x1B, '%', '0', 'B' };
        final byte[] enterPclMode = { 0x1B, '%', '0', 'A' };
        final byte[] hpglCommand = { 'I', 'N', ';' };

        final ByteArrayOutputStream work = new ByteArrayOutputStream();
        work.write(parameterizedPclCommandContent);
        work.write("@PJL ENTER LANGUAGE=PCL\r\n".getBytes("iso-8859-1"));
        work.write(twoByteCommandContent);
        work.write(textCommandContent);
        work.write(enterHpglMode);
        work.write(hpglCommand);
        work.write(enterPclMode);
        work.write(controlCharacterContent);

        final ByteArrayInputStream data = new ByteArrayInputStream(work.toByteArray());
        final PclInputStreamForInputStream pclStream = new PclInputStreamForInputStream(data);
        new Pcl5Parser(new PclParserContext(pclStream, this)).parse();

        pclStream.close();

        assertEquals(1, VISITOR.textCommandCounter);
        assertEquals(1, VISITOR.controlCharacterCounter);
        assertEquals(1, VISITOR.twoBytePclCommandCounter);
        assertEquals(3, VISITOR.parameterizedPclCommand);
        assertEquals(1, VISITOR.pjlCommand);
        assertEquals(1, VISITOR.hpglCommand);
    }
}
