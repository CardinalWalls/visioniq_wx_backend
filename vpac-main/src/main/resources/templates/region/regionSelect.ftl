<#macro regionSelect type="">
  <#if type == "region">
        <div class="form-group col-xs-4 col-sm-4 col-lg-4 col-md-4">
          <label class="control-label">一级地区：</label>
          <div>
            <select class="form-control region_select" name="regionFirst" id="region_select_regionFirst"
                    data-second-id="region_select_regionSecond" data-third-id="region_select_regionThird"
                    required>
              <option>-请选择-</option>
            </select>
          </div>
        </div>
        <div class="form-group col-xs-4 col-sm-4 col-lg-4 col-md-4">
          <label class="control-label ">二级地区：</label>
          <div>
            <select class="form-control region_select" name="regionSecond" id="region_select_regionSecond"
                    data-third-id="region_select_regionThird" required>
              <option>-请选择-</option>
            </select>
          </div>
        </div>
        <div class="form-group col-xs-4 col-sm-4 col-lg-4 col-md-4">
          <label class="control-label">三级地区：</label>
          <div>
            <select class="form-control region_select" name="regionThird" id="region_select_regionThird"
                    data-children="" required>
              <option>-请选择-</option>
            </select>
          </div>
        </div>
  </#if>

  <#if type == "regionForSearch">
    <div class="form-group">
      <select type="text" placeholder="一级地区" name="regionFirst" id="region_select_regionFirst_search"
              class="form-control region_select" data-second-id="region_select_regionSecond_search"
              data-third-id="region_select_regionThird_search" title="一级地区"></select>
    </div>
    <div class="form-group">
      <select type="text" placeholder="二级地区" name="regionSecond" id="region_select_regionSecond_search"
              class="form-control region_select" data-third-id="region_select_regionThird_search"
              title="二级地区"></select>
    </div>
    <div class="form-group">
      <select type="text" placeholder="三级地区" name="regionThird" id="region_select_regionThird_search"
              class="form-control region_select" title="三级地区"></select>
    </div>
  </#if>
</#macro>