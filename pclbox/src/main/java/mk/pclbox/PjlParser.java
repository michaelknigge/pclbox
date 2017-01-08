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

import java.io.IOException;

/**
 * This {@link PjlParser} parses PJL commands. It is constructed from the {@link Pcl5Parser}
 * and will return control to the {@link Pcl5Parser} after an escape bye (0x1B) has been read from
 * the data stream.
 */
final class PjlParser extends DataStreamParser {

    /**
     * Constructor. Just gets the stream.
     *
     * @param stream - the stream that contains the PJL commands.
     */
    PjlParser(final PclParserContext context) {
        super(context);
    }

    @Override
    public int parse() throws IOException, PclException {
        // TODO implement PJL parser!
        return -1;
    }
}
