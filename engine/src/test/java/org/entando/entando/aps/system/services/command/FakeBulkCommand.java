package org.entando.entando.aps.system.services.command;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.exception.ApsSystemException;

public class FakeBulkCommand extends BaseBulkCommand<String, Object> {

	public FakeBulkCommand(Collection<String> items, Object applier, BulkCommandTracer<String> tracer) {
		super(items, applier, tracer);
	}

	public FakeBulkCommand(Collection<String> items, Object applier, BulkCommandTracer<String> tracer, 
			CountDownLatch startSignal, CountDownLatch endSignal) {
		super(items, applier, tracer);
		this._startSignal = startSignal;
		this._endSignal = endSignal;
	}

	@Override
	public void apply() {
		try {
			if (this._startSignal != null) {
				this._startSignal.await();
			}
			super.apply();
			if (this._endSignal != null) {
				this._endSignal.countDown();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected boolean apply(String item) throws ApsSystemException {
		boolean result = true;
		if (item.contains("err")) {
			this.getTracer().traceError(item, ApsCommandErrorCode.ERROR);
			result = false;
		} if (item.contains("warn")) {
			this.getTracer().traceWarning(item, ApsCommandWarningCode.NOT_NECESSARY);
		} else {
			this.getTracer().traceSuccess(item);
		}
		return result;
	}

	private CountDownLatch _startSignal;
	private CountDownLatch _endSignal;

}