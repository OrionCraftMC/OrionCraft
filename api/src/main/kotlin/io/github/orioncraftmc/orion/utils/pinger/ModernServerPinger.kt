package io.github.orioncraftmc.orion.utils.pinger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.orioncraftmc.orion.utils.pinger.model.ServerPingResult
import io.github.orioncraftmc.orion.utils.pinger.model.ServerPingVersionData
import io.github.orioncraftmc.orion.utils.pinger.model.players.ServerPingResultPlayerData
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


object ModernServerPinger : ServerPinger {

	private val mapper = jacksonObjectMapper()

	private const val SEGMENT_BITS = 0x7F
	private const val CONTINUE_BIT = 0x80

	private fun DataInputStream.readVarInt(): Int {
		var value = 0
		var position = 0
		var currentByte: Byte
		while (true) {
			currentByte = readByte()
			value = value or (currentByte.toInt() and SEGMENT_BITS shl position)
			if (currentByte.toInt() and CONTINUE_BIT == 0) break
			position += 7
			if (position >= 32) throw RuntimeException("VarInt is too big")
		}
		return value
	}


	@Throws(IOException::class)
	private fun DataOutputStream.writeVarInt(paramInt: Int) {
		var result = paramInt
		while (true) {
			if (result and -0x80 == 0) {
				writeByte(result)
				return
			}
			writeByte(result and 0x7F or 0x80)
			result = result ushr 7
		}
	}

	private fun createHandshakePacket(address: String, port: Int): ByteArray {
		return ByteArrayOutputStream().use { baos ->
			DataOutputStream(baos).use { dos ->
				dos.writeByte(0x00) //packet id for handshake

				dos.writeVarInt(759) //protocol version
				dos.writeVarInt(address.length) //host length

				dos.writeBytes(address) //host string
				dos.writeShort(port) //port
				dos.writeVarInt(1) //state (1 for handshake)
			}

			baos.toByteArray()
		}
	}


	override fun ping(address: String, port: Int): ServerPingResult? = runCatching {
		val socket = Socket().apply { soTimeout = 1000 }

		socket.connect(InetSocketAddress(address, port), 1000)

		val inStr = DataInputStream(socket.getInputStream())
		val outStr = DataOutputStream(socket.getOutputStream())

		/* Send Handshake packet to server */
		val handshake = createHandshakePacket(address, port)
		outStr.writeVarInt(handshake.size)
		outStr.write(handshake)
		outStr.flush()


		/* Send Status Request packet to server */
		outStr.writeByte(0x1) // size
		outStr.writeByte(0x0) // packet id for status request
		outStr.flush()

		/* Read response from server */

		val responseSize = inStr.readVarInt() // size
		val id = inStr.readVarInt() // packet id for status response

		if (id == -1) throw IOException("Premature end of stream")
		if (id != 0x00) throw IOException("Invalid ping response packet id")

		val jsonSize = inStr.readVarInt() // size of json
		val jsonBytes = ByteArray(jsonSize)
		inStr.readFully(jsonBytes) // json

		/* Send Ping request to server - this will allow us to measure ping time */

		outStr.writeByte(0x09) // size of pong
		outStr.writeByte(0x01) // packet id for pong
		outStr.writeLong(System.currentTimeMillis()) // time sent
		outStr.flush()

		val beforePing = System.currentTimeMillis()

		/* Read pong response from server */
		val pongSize = inStr.readVarInt() // size
		val id2 = inStr.readVarInt() // packet id for pong
		if (id2 == -1) throw IOException("Premature end of stream")
		if (id2 != 0x01) throw IOException("Invalid pong response packet id")


		val pingTime = System.currentTimeMillis() - beforePing

		inStr.close()
		outStr.close()
		socket.close()


		val response = mapper.readTree(jsonBytes)

		val descriptionNode = response.get("description")
		val motd = if (descriptionNode.isTextual) {
			LegacyComponentSerializer.legacySection().deserialize(descriptionNode.textValue())
		} else {
			GsonComponentSerializer.gson().deserialize(descriptionNode.toString())
		}

		val versionNode = response.get("version")
		val versionData = ServerPingVersionData(
			versionNode.get("name").textValue(),
			versionNode.get("protocol").intValue()
		)

		val players = ServerPingResultPlayerData(
			response.get("players").get("online").intValue(),
			response.get("players").get("max").intValue()
		)

		val icon = response.get("favicon").textValue()

		return ServerPingResult(motd, versionData, players, icon, pingTime.toInt())
	}.getOrNull()
}
