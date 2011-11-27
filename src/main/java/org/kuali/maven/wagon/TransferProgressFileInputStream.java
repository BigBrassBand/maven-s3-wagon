/**
 * Copyright 2004-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.wagon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * An extension to the {@link FileInputStream} that notifies a @{link TransferProgress} object as it is being read from
 * 
 * @author Ben Hale
 * @since 1.1
 */
public class TransferProgressFileInputStream extends FileInputStream {

	private TransferProgress progress;

	public TransferProgressFileInputStream(File file, TransferProgress progress) throws FileNotFoundException {
		super(file);
		this.progress = progress;
	}

	public int read() throws IOException {
		int b = super.read();
		if (b != -1) {
			progress.notify(new byte[] { (byte) b }, 1);
		}
		return b;
	}

	public int read(byte b[]) throws IOException {
		int length = super.read(b);
		if (length != -1) {
			progress.notify(b, length);
		}
		return length;
	}

	public int read(byte b[], int off, int len) throws IOException {
		int count = super.read(b, off, len);
		if (count == -1) {
			return count;
		}
		if (off == 0) {
			progress.notify(b, count);
		} else {
			byte[] bytes = new byte[len];
			System.arraycopy(b, off, bytes, 0, count);
			progress.notify(bytes, count);
		}
		return count;
	}
}
