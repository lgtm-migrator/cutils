/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2022 Bobai Kato. All Rights Reserved.
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

package art.cutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Assists with the serialization process and performs additional functionality based on
 * serialization.
 *
 * <ul>
 *   <li>Deep clone using serialization
 *   <li>Serialize managing finally and IOException
 *   <li>Deserialize managing finally and IOException
 * </ul>
 *
 * <p>This class throws exceptions for invalid {@code null} inputs. Each method documents its
 * behaviour in more detail.
 *
 * <p>#ThreadSafe#
 *
 * @author @author <a href="https://github.com/B0BAI">Bobai Kato</a>
 * @since 1.0
 */
public class Serialization extends SerializationUtils {

  /**
   * Serializes an {@code Object} to a byte array.
   *
   * @param object the object to serialize to bytes
   * @return a byte[] with the converted Serializable
   * @throws java.io.IOException if the serialization fails
   * @since 1.0
   */
  public static byte @NotNull [] serialize(final Object object) throws IOException {
    Objects.requireNonNull(object, "Object to serialize cannot be null.");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
    final ObjectOutputStream os = new ObjectOutputStream(outputStream);
    os.writeObject(object);
    os.flush();
    os.close();
    return outputStream.toByteArray();
  }
}