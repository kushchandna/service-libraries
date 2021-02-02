package com.kush.lib.indexing;

import com.kush.utils.testhelpers.SampleObject;
import com.kush.utils.testhelpers.SampleObjectsTestRepository;

public class IndexableSampleObjectsTestRepository extends SampleObjectsTestRepository {

    private UpdateHandler<SampleObject> updateHandler;

    public void setUpdateHandler(UpdateHandler<SampleObject> updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        updateHandler = null;
    }

    @Override
    protected void after() {
        super.after();
        updateHandler = null;
    }

    @Override
    public SampleObject add(SampleObject object) {
        SampleObject oldObject = super.add(object);
        updateHandler.onUpdate(oldObject, object);
        return oldObject;
    }

    @Override
    public SampleObject remove(String id) {
        SampleObject object = super.remove(id);
        updateHandler.onRemove(object);
        return object;
    }
}
