package application;

public class SplitPacketManager {
	
	static String[] splitMultiplePacket(String packet) {

		String _partitioningPacket[] = packet.split(Settings.sReceiverSplitMultipleToken);
		return _partitioningPacket;

	}

	/**
	 * split the packet base on the programmer's rule
	 * 
	 * @param packet
	 * @return
	 */
	static String[] splitProtocol(String packet) {

		String _partitioningPacket[] = packet.split(Settings.sReceiverSplitProtocolToken);

		for (int i = 0; i < _partitioningPacket.length - 1; i++) {
			_partitioningPacket[i] = _partitioningPacket[i + 1];
		}

		_partitioningPacket[_partitioningPacket.length - 1] = null;

		return _partitioningPacket;

	}
}
