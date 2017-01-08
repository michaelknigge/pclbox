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

/**
 * Abstract class that every {@link PrinterCommand} has to implement.
 */
abstract class PrinterCommand {

    private final long offset;

    /**
     * Constructor. Gets just the offset of the {@link PrinterCommand}.
     *
     * @param offset - offset of this {@link PrinterCommand}, measured from the beginning of the read data stream.
     */
    public PrinterCommand(final long offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset of this {@link PrinterCommand}, measured from the beginning
     * of the read data stream.
     *
     * @return the offset of the {@link PrinterCommand}.
     */
    public long getOffset() {
        return this.offset;
    }

    /**
     * Calculates a hash for the offset.
     *
     * @return the hash code (32bit) for the offset (64 bit).
     */
    protected int getOffsetHash() {
        return (int) (this.offset ^ (this.offset >>> 32));
    }

    /**
     * Delegates the concrete {@link PrinterCommand} to the corresponding handle
     * method of the {@link PrinterCommandVisitor}.
     *
     * @param visitor a {@link PrinterCommandVisitor}.
     */
    abstract void accept(PrinterCommandVisitor visitor);
}
