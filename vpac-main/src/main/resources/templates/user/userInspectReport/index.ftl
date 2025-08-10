<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="检查日期开始" name="inspectDateS" id="inspectDateS" class="form-control layer-date" title="检查日期开始">
        </div>
        ~
        <div class="form-group">
          <input type="text" placeholder="检查日期截止" name="inspectDateE" id="inspectDateE" class="form-control layer-date" title="检查日期截止">
        </div>
        |
        <div class="form-group">
          <input type="text" placeholder="身份证" name="idcard" class="form-control" title="身份证">
        </div>
        <div class="form-group">
          <input type="text" placeholder="姓名" name="name" class="form-control" title="姓名">
        </div>
        <div class="form-group">
          <select name="gender" class="form-control" title="性别">
            <option value="">- 性别 -</option>
            <option value="1">男</option>
            <option value="2">女</option>
          </select>
        </div>
        <div class="form-group">
          <input type="text" placeholder="学校" name="school" class="form-control" title="学校">
        </div>
        <div class="form-group">
          <input type="text" placeholder="导入批次" name="importNum" class="form-control" title="导入批次">
        </div>
        <div class="form-group">
          <input type="text" placeholder="关联账号手机" name="accountPhone" class="form-control" title="关联账号手机">
        </div>
        <div class="form-group">
          <select name="hasArchive" class="form-control" title="是否关联账号">
            <option value="">- 是否关联账号 -</option>
            <option value="true">已关联账号</option>
            <option value="false">未关联账号</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="userInspectReport.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="userInspectReport.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="userInspectReport.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="userInspectReport.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
        <button onclick="userInspectReport.showImportData()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          批量导入
        </button>
        <button onclick="userInspectReport.importNumDelete()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          批次删除
        </button>
      </div>
    <table id="userInspectReport_table_list" class="table-striped text-nowrap"></table>
  </div>
</div>
<div class="modal" id="userInspectReport_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
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
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">身份证 <b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="idcard" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">姓名 <b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="name" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">性别：</label>
            <div>
              <select class="form-control" name="gender" required>
                <option value="1">男</option>
                <option value="2">女</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">检查日期：</label>
            <div>
              <input type="text" class="form-control layer-date" name="inspectDate" required/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">学校：</label>
            <div>
              <input type="text" class="form-control" name="school" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">班级：</label>
            <div>
              <input type="text" class="form-control" name="className" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">检查医院：</label>
            <div>
              <input type="text" class="form-control" name="hospital" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">当前身高cm：</label>
            <div>
              <input type="number" class="form-control" name="height"  />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">右眼裸眼(RV)：</label>
            <div>
              <input type="text" class="form-control" name="rightVisual" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼球镜(R/S)：</label>
            <div>
              <input type="text" class="form-control" name="rightDiopterS" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼柱镜(R/C)：</label>
            <div>
              <input type="text" class="form-control" name="rightDiopterC" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼轴位(R/A)：</label>
            <div>
              <input type="text" class="form-control" name="rightShaftPosition" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">左眼裸眼(LV)：</label>
            <div>
              <input type="text" class="form-control" name="leftVisual" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼球镜(L/S)：</label>
            <div>
              <input type="text" class="form-control" name="leftDiopterS" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼柱镜(L/C)：</label>
            <div>
              <input type="text" class="form-control" name="leftDiopterC" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼轴位(L/A)：</label>
            <div>
              <input type="text" class="form-control" name="leftShaftPosition" />
            </div>
          </div>

          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">左眼眼轴：</label>
            <div>
              <input type="text" class="form-control" name="leftAxis" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼曲率半径：</label>
            <div>
              <input type="text" class="form-control" name="leftCurvatureRadius" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼K1：</label>
            <div>
              <input type="text" class="form-control" name="leftK1" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">左眼K2：</label>
            <div>
              <input type="text" class="form-control" name="leftK2" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">右眼眼轴：</label>
            <div>
              <input type="text" class="form-control" name="rightAxis" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼曲率半径：</label>
            <div>
              <input type="text" class="form-control" name="rightCurvatureRadius" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼K1：</label>
            <div>
              <input type="text" class="form-control" name="rightK1" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">右眼K2：</label>
            <div>
              <input type="text" class="form-control" name="rightK2" />
            </div>
          </div>

          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">校外用眼时长：</label>
            <div>
              <input type="text" class="form-control" name="outSchoolHours"/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">坐姿不正确：</label>
            <div>
              <input type="text" class="form-control" name="incorrectSittin"/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">佩戴眼镜不正确：</label>
            <div>
              <input type="text" class="form-control" name="incorrectGlasses" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">过敏情况：</label>
            <div>
              <input type="text" class="form-control" name="allergy" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">眼镜种类：</label>
            <div>
              <input type="text" class="form-control" name="glassesType" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">其它措施：</label>
            <div>
              <input type="text" class="form-control" name="otherSolution" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">户外用眼时长：</label>
            <div>
              <input type="text" class="form-control" name="outdoorsHours" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">其它说明：</label>
            <div>
              <input type="text" class="form-control" name="otherDescription"/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-3">
            <label class="control-label">户外用眼时长说明：</label>
            <div>
              <input type="text" class="form-control" name="outdoorsHoursExplain" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-3">
            <label class="control-label">眼镜种类说明：</label>
            <div>
              <input type="text" class="form-control" name="glassesTypeExplain" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-6">
            <label class="control-label">其它措施说明：</label>
            <div>
              <input type="text" class="form-control" name="otherSolutionExplain" />
            </div>
          </div>

          <div class="form-group form-line-l col-xs-6">
            <label class="control-label">检查单据：</label>
            <button type="button" class="btn btn-white btn-xs" onclick="attachment.uploadFile(5, 'fileArray')">
              <i class="fa fa-upload"></i> 上传
            </button>
            <div>
              <input class="form-control attachment-field" name="fileArray" id="fileArray"/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-6">
            <label class="control-label">其它单据：</label>
            <button type="button" class="btn btn-white btn-xs" onclick="attachment.uploadFile(5, 'otherFileArray')">
              <i class="fa fa-upload"></i> 上传
            </button>
            <div>
              <input class="form-control attachment-field" name="otherFileArray" id="otherFileArray"/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="userInspectReport.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>

<div class="modal" id="import_modal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            批量导入新增工单
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal">
            <div class="form-group">
              <label class="control-label">数据开始行数<b class="text-danger">*</b>：</label>
              <div>
                <input type="text" class="form-control" name="startRow" initValue="2" required/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label">导入模板文件<b class="text-danger">*</b>：</label>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(1, 'dataFile')">
                <i class="fa fa-upload"></i> 上传
              </button>
              <a class="m-l" href="/admin/pub/js/user/userInspectReport/inspect_report_import.xlsx" target="_blank" download="检查报告导入模板.xlsx">
                <b>下载导入模板<span class="text-danger">（请勿修改模板列的顺序）</span></b>
              </a>
              <div>
                <input class="form-control attachment-field" name="dataFile" id="dataFile" required/>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="userInspectReport.importData()">
            <i class="fa fa-save"></i> 保存
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i> 关闭</button>
        </div>
      </div>
    </div>
</div>
<script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
<script src="${ctx!}/admin/pub/js/user/userInspectReport/index.js"></script>

</@bodyContent>