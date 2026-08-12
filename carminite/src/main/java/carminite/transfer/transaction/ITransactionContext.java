package carminite.transfer.transaction;


public sealed interface ITransactionContext permits Transaction {
	int depth();
}