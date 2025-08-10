<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="姓名" name="name" class="form-control" title="姓名">
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="healthyForm.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
<#--        <button onclick="healthyForm.add()" type="button" class="btn btn-info">-->
<#--          <i class="fa fa-plus"></i>-->
<#--          新增-->
<#--        </button>-->
        <button onclick="healthyForm.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="healthyForm.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
      </div>
    <table id="healthyForm_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="healthyForm_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
              class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal">
          <input type="hidden" class="form-control" name="id"/>
          <input type="hidden" class="form-control" name="userId"/>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">姓名：</label>
            <div>
              <input type="text" class="form-control" name="name" required/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">性别：</label>
            <div>
              <select class="form-control" name="gender" >
                <option value="1">男</option>
                <option value="2">女</option>
                <option value="0">未填写</option>
              </select>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">年龄：</label>
            <div>
              <input type="number" min="0" class="form-control" name="age" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">身高cm：</label>
            <div>
              <input type="number" min="0" class="form-control" name="height" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">体重kg：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weight" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">BMI：</label>
            <div>
              <input type="number" min="0" class="form-control" name="bmi" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">近一个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightOneMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">近二个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightTwoMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">近三个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightThreeMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">近四个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightFourMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">近五个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightFiveMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">近六个月体重：</label>
            <div>
              <input type="number" min="0" class="form-control" name="weightSixMonth" initValue="0"/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">基础疾病：</label>
            <div>
              <input type="text" class="form-control" name="diseases" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">早餐主食：</label>
            <div>
              <input type="text" class="form-control" name="breakfastMain" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">早餐蔬菜：</label>
            <div>
              <input type="text" class="form-control" name="breakfastVegetable" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">早餐肉类：</label>
            <div>
              <input type="text" class="form-control" name="breakfastMeat" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">早餐豆类：</label>
            <div>
              <input type="text" class="form-control" name="breakfastBean" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">早餐奶类：</label>
            <div>
              <input type="text" class="form-control" name="breakfastMilk" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">早餐水果：</label>
            <div>
              <input type="text" class="form-control" name="breakfastFruits" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">午餐主食：</label>
            <div>
              <input type="text" class="form-control" name="lunchMain" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">午餐蔬菜：</label>
            <div>
              <input type="text" class="form-control" name="lunchVegetable" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">午餐肉类：</label>
            <div>
              <input type="text" class="form-control" name="lunchMeat" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">午餐豆类：</label>
            <div>
              <input type="text" class="form-control" name="lunchBean" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">午餐奶类：</label>
            <div>
              <input type="text" class="form-control" name="lunchMilk" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">午餐水果：</label>
            <div>
              <input type="text" class="form-control" name="lunchFruits" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">晚餐主食：</label>
            <div>
              <input type="text" class="form-control" name="dinnerMain" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">晚餐蔬菜：</label>
            <div>
              <input type="text" class="form-control" name="dinnerVegetable" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">晚餐肉类：</label>
            <div>
              <input type="text" class="form-control" name="dinnerMeat" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">晚餐豆类：</label>
            <div>
              <input type="text" class="form-control" name="dinnerBean" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">晚餐奶类：</label>
            <div>
              <input type="text" class="form-control" name="dinnerMilk" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">晚餐水果：</label>
            <div>
              <input type="text" class="form-control" name="dinnerFruits" />
            </div>
          </div>
          <div class="form-group col-xs-12 form-line-l">
            <label class="control-label">保健品：</label>
            <div>
              <input type="text" class="form-control" name="healthProducts" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-l">
            <label class="control-label">体力活动：</label>
            <div>
              <input type="text" class="form-control" name="physicalLabour" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">运动方式：</label>
            <div>
              <input type="text" class="form-control" name="sportInfo" />
            </div>
          </div>
          <div class="form-group col-xs-4 form-line-r">
            <label class="control-label">运动频率：</label>
            <div>
              <input type="text" class="form-control" name="sportFrequency" />
            </div>
          </div>
          <div class="form-group col-xs-12 form-line-l">
            <label class="control-label">大便情况：</label>
            <div>
              <input type="text" class="form-control" name="shitInfo" />
            </div>
          </div>
          <div class="form-group col-xs-12 form-line-l">
            <label class="control-label">睡眠情况：</label>
            <div>
              <input type="text" class="form-control" name="sleepInfo" />
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="healthyForm.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/user/healthyForm/index.js"></script>

</@bodyContent>